package com.tracker.portfolio.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.tracker.portfolio.dto.PositionDTO;
import com.tracker.portfolio.dto.StockDTO;
import com.tracker.portfolio.entity.Stock;
import com.tracker.portfolio.mapper.StockMapper;
import com.tracker.portfolio.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    private final String API_KEY = System.getenv("ALPHA_API_KEY");

    public Optional<Stock> findByTickerInDB(String ticker) {
        return stockRepository.findByTicker(ticker.toUpperCase());
    }

    public Stock getStock(PositionDTO positionDTO) {
        String ticker = positionDTO.getTicker();
        Optional<Stock> stockOptional = findByTickerInDB(ticker);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            return dataIsOld(stock) ? updateStockEntity(stock) : stock;
        }
        return getStockEntityFromNetwork(positionDTO.getTicker(), positionDTO.getStockExchange());
    }

    private boolean dataIsOld(Stock stock) {
        LocalDate stockDate = stock.getDate();
        LocalDate now = LocalDate.now();
        if (DAYS.between(stockDate, now) < 2) {
            return false;
        }
        DayOfWeek dayOfWeekStock = stockDate.getDayOfWeek();
        DayOfWeek dayOfWeekNow = now.getDayOfWeek();
        return !dayOfWeekStock.equals(DayOfWeek.FRIDAY) || !dayOfWeekNow.equals(DayOfWeek.MONDAY);
    }

    private Stock updateStockEntity(Stock stock) {
        Stock stockNew = getStockEntityFromNetwork(stock.getTicker(), stock.getStockExchange());
        stock.setValue(stockNew.getValue());
        stock.setDate(stockNew.getDate());
        return save(stock);
    }

    private Stock getStockEntityFromNetwork(String ticker, String stockExchange) {
        if ("WSE".equals(stockExchange)) {
            return getStockEntityForWSE(ticker);
        } else {
            return getStockEntityForUSA(ticker);
        }
    }

    private Stock getStockEntityForWSE(String ticker) {
        String priceWSE = getPriceWSE(ticker);
        return new Stock(ticker, new BigDecimal(priceWSE), "WSE", LocalDate.now());
    }

    private String getPriceWSE(String ticker) {
        String url = "https://www.biznesradar.pl/notowania/" + ticker;
        WebClient webClient = getStaticWebClient();
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<?> buckets = page.getByXPath("//span/span[@class='q_ch_act']");
        if (!buckets.isEmpty()) {
            return ((HtmlSpan) buckets.get(0)).getFirstChild().asText();
        }
        return "-1";
    }

    private WebClient getStaticWebClient() {
        WebClient webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        return webClient;
    }

    private Stock getStockEntityForUSA(String ticker) {
        StockDTO stockDTO = getStockDTOFromAlphaVantage(ticker);
        return stockMapper.getStockEntityUSA(stockDTO);
    }

    private StockDTO getStockDTOFromAlphaVantage(String ticker) {
        String url = getAlphaVantageUrl(ticker);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        StockDTO stockDTO = null;
        try {
            ResponseEntity<StockDTO> result = restTemplate.exchange(url, HttpMethod.GET, request,
                    new ParameterizedTypeReference<StockDTO>() {
                    });
            if (result.getStatusCode().equals(HttpStatus.OK)) {
                stockDTO = result.getBody();
            } else {
            }
        } catch (HttpClientErrorException e) {
        }
        return stockDTO;
    }

    private String getAlphaVantageUrl(String ticker) {
        return "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&apikey=" + API_KEY;
    }

    private Stock save(Stock stock) {
         return stockRepository.save(stock);
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

}
