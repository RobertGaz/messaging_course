package my;

import my.model.Receipt;
import my.model.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ReceiptProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptProcessor.class);
    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    private FailedReceiptSender failedReceiptSender;

    public void processReceipt(Receipt receipt, Region region, Long processingAttempts) {
        if (processingAttempts < MAX_ATTEMPTS) {
            if (processAttempt(receipt)) {
                LOGGER.info("was able to process receipt successfully");
                return;
            }
            LOGGER.info("was not able to process receipt successfully, dispatching to receipt-retry-exchange...");
            processingAttempts++;
            failedReceiptSender.dispatchReceiptToRetry(receipt, region, processingAttempts);
        } else {
            LOGGER.info("was not able to process receipt successfully, dispatching to failed-receipt-exchange...");
            failedReceiptSender.dispatchFailedReceipt(receipt, region);
        }

    }


    // returns true if processing successful, false otherwise
    private boolean processAttempt(Receipt receipt) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        Random rand = new Random();
        int val = rand.nextInt(100);
        return val < 30;
    }

}