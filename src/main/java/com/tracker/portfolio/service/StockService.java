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


    public Optional<Stock> findByTicker(String ticker) {
        return stockRepository.findByTicker(ticker.toUpperCase());
    }

    public Stock getStock(PositionDTO positionDTO) {
        String ticker = positionDTO.getTicker();
        Optional<Stock> stockOptional = findByTicker(ticker);
        if (stockOptional.isPresent()) {
            return stockOptional.get();
        }
        String stockExchange = positionDTO.getStockExchange();
        BigDecimal price = getStockPrice(stockExchange, ticker);
        return new Stock(ticker, price, stockExchange, LocalDate.now());
    }

    public BigDecimal getStockPrice(String stockExchange, String ticker) {
        Optional<BigDecimal> priceOptional = getUpdatedStockValue(ticker);
        if (priceOptional.isPresent()) {
            return priceOptional.get();
        } else {
            Stock stock = getStockEntityFromNetwork(stockExchange, ticker);
            return stock.getValue();
        }
    }

    private Optional<BigDecimal> getUpdatedStockValue(String ticker) {
        Optional<Stock> stockOptional = findByTicker(ticker);
        if (stockOptional.isPresent()) {
            Stock stock = stockOptional.get();
            if (dataIsOld(stock)) {
                updateStockEntity(stock);
            }
            return Optional.of(stock.getValue());
        } else {
            return Optional.empty();
        }
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

    private void updateStockEntity(Stock stock) {
        Stock stockNew = getStockEntityFromNetwork(stock.getStockExchange(), stock.getTicker());
        stock.setValue(stockNew.getValue());
        stock.setDate(stockNew.getDate());
        stockRepository.save(stock);
    }

    private Stock getStockEntityForUSA(String ticker) {
        StockDTO stockDTO = getStockDTOFromAlphaVantage(ticker);
        return stockMapper.getStockEntityUSA(stockDTO);
    }

    public StockDTO getStockDTOFromAlphaVantage(String ticker) {
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

    private Stock getStockEntityForWSE(String ticker) {
        String priceWSE = getPriceWSE(ticker);
        return new Stock(ticker, new BigDecimal(priceWSE), "WSE", LocalDate.now());
    }

    public String getPriceWSE(String ticker) {
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

    private Stock getStockEntityFromNetwork(String stockExchange, String ticker) {
        if ("WSE".equals(stockExchange)) {
            return getStockEntityForWSE(ticker);
        } else {
            return getStockEntityForUSA(ticker);
        }
    }

    private void save(Stock stock) {
        stockRepository.save(stock);
    }

}
