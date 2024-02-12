package andreiredkouski.tradeservice.service.infrastructure.controller;


import andreiredkouski.tradeservice.service.trade.TradeService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/trade")
public class TradeController {

    private static final String CSV_CONTENT_TYPE = "text/csv";
    private static final String CONTENT_DISPOSITION_HEADER_KEY = "Content-Disposition";
    private static final String CONTENT_DISPOSITION_HEADER_VALUE = "attachment; filename=\"trades.csv\"";

    private final TradeService tradeService;

    public TradeController(final TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping
    public void getTrade(@RequestParam("file") final MultipartFile file,
                                        final HttpServletResponse response
    ) throws IOException {
        response.setContentType(CSV_CONTENT_TYPE);
        response.addHeader(CONTENT_DISPOSITION_HEADER_KEY, CONTENT_DISPOSITION_HEADER_VALUE);
        tradeService.getTradesWithProductNames(file, response.getWriter());
    }
}
