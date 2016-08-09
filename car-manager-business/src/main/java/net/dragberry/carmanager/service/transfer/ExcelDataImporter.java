package net.dragberry.carmanager.service.transfer;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import net.dragberry.carmanager.transferobject.Record;
import net.dragberry.carmanager.transferobject.TransactionTO;

/**
 * Excel data importer
 * 
 * @author Maksim Drahun
 *
 */
@Service("ExcelDataImporter")
public class ExcelDataImporter implements DataImporter {
	
	private static final Logger LOG = LogManager.getLogger(ExcelDataImporter.class);
	
	private static final int QUEUE_LIMIT = 20;
	
	@Override
	public void doImport(InputStream is) throws Exception {
		BlockingQueue<TransactionTO> transactionQueue = new ArrayBlockingQueue<>(QUEUE_LIMIT);
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		Reader reader = new Reader(is, transactionQueue);
		executor.submit(reader);
	}
	
	private static class Reader implements Callable<Object> {
		
		private final InputStream is;
		
		private StringBuilder sb;
		
		private final BlockingQueue<TransactionTO> transactionQueue;
		
		public Reader(InputStream is, BlockingQueue<TransactionTO> transactionQueue) {
			this.is = is;
			this.transactionQueue = transactionQueue;
		}

		@Override
		public Object call() throws Exception {
			try (XSSFWorkbook wb = new XSSFWorkbook(is)) {
				Sheet sheet = wb.getSheetAt(0);
				sheet.forEach((row) -> {
					if (row.getRowNum() >= 4) {
						sb = new StringBuilder("Row #").append(row.getRowNum()).append("\t");
						Record record = getRecord(row);
						TransactionTO transaction = buildTransaction(record);
						try {
							transactionQueue.put(transaction);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					System.out.println(sb);
				});
			}
			return null;
		}

		/**
		 * Builds transaction from record
		 * 
		 * @param record
		 * @return
		 */
		private TransactionTO buildTransaction(Record record) {
			TransactionTO transaction = new TransactionTO();
			
			
			return transaction;
		}

		/**
		 * Get the record from row
		 * 
		 * @param row
		 * @return
		 */
		private Record getRecord(Row row) {
			Record record = new Record();
			row.forEach(cell -> {
				addCellInfo(sb, cell, cell.getColumnIndex());
				processCell(cell, record);
			});
			return record;
		}
		
		/**
		 * Processes single cell
		 * 
		 * @param cell
		 * @param record
		 */
		private void processCell(Cell cell, Record record) {
			switch (cell.getColumnIndex()) {
			case 0:
				record.setIndex((long) cell.getNumericCellValue());
				break;
			case 1:
				record.setDate(cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				break;
			case 2:
				record.setDescription(cell.getStringCellValue());
				break;
			case 3:
				record.setType(cell.getStringCellValue());
				break;
			case 5:
				record.setCostBYR(cell.getNumericCellValue());
				break;
			case 6:
				record.setCostUSD(cell.getNumericCellValue());
				break;
			case 7:
				record.setCostBYRDad(cell.getNumericCellValue());
				break;
			case 8:
				record.setCostUSDDad(cell.getNumericCellValue());
				break;
			case 9:
				record.setTax(cell.getNumericCellValue());
				break;
			case 10:
				record.setExchangeRate(cell.getNumericCellValue());
				break;
			case 11:
				record.setExchangeRateReal(cell.getNumericCellValue());
				break;
			}
		}

		private void addCellInfo(StringBuilder sb, Cell cell, int cellNum) {
			if (cell == null) {
				return;
			}
			sb.append("Cell #").append(cellNum).append("[");
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				sb.append("BLANK]").append(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				sb.append("NUMERIC]").append(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				sb.append("STRING]").append(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				sb.append("FORMULA]").append(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				sb.append("BOOLEAN]").append(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				sb.append("ERROR]").append(cell.getErrorCellValue());
				break;
			}
			sb.append(" ");
		}
	}
	
	private static class Processor implements Callable<Object> {

		private final BlockingQueue<TransactionTO> transactionQueue;
		
		public Processor(BlockingQueue<TransactionTO> transactionQueue) {
			this.transactionQueue = transactionQueue;
		}
		
		@Override
		public Object call() throws Exception {
			TransactionTO transaction = null;
			while ((transaction = transactionQueue.poll(100, TimeUnit.MILLISECONDS)) != null) {
				processTransaction(transaction);
			}
			return null;
		}

		/**
		 * Process transaction
		 * 
		 * @param transaction
		 */
		private void processTransaction(TransactionTO transaction) {
			
		}
		
	}
}