package com.roma.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;

@WebServlet("/number/*")
public class RandomServlet extends HttpServlet {
	
	private static final String REGION_NUMBER = "116";
	private static final String REGION_SYMBOL = "RUS";
	private static final int MAX_NUMBER = 1000;
	private static final String MAX_SYMBOL = "Х";
	private String[] letters = {"А", "В", "Е", "К", "М", "Н", "О", "Р", "С", "Т", "У", "Х"};
	private int[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private String firstSymbol, secondSymbol, thirdSymbol;
	private int firstNumber, secondNumber, thirdNumber;
	private String stateNumber;
	private Set<String> stateNumberSet = new HashSet<String>();
	private int numberInt;
	private String numberString;
	private int count = 0;
	private int firstSymbolIndex, secondSymbolIndex, thirdSymbolIndex;
	private boolean flag;
	
	public Set<String> getStateNumberSet() {
		return stateNumberSet;
	}

	public void setStateNumberSet(Set<String> stateNumberSet) {
		this.stateNumberSet = stateNumberSet;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		String info = req.getPathInfo();
		System.out.println(info);
		if (info.equals("/random")) {
			getRandomNumber(req, resp);
		} else if (info.equals("/next")) {
			getNextNumber(req, resp);
		} else {
			resp.setContentType("text/plain; charset=windows-1251");
			PrintWriter printWriter = resp.getWriter();
			printWriter.write("Add '/random' or '/next' after '/number'");
			printWriter.close();
		}
		
	}
	
	@GetMapping("/random")
	public void getRandomNumber(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		count = 0;
		firstSymbol = getRandomSymbol(letters);
		firstNumber = getRandomNumber(numbers);
		secondNumber = getRandomNumber(numbers);
		thirdNumber = getRandomNumber(numbers);
		secondSymbol = getRandomSymbol(letters);
		thirdSymbol = getRandomSymbol(letters);
		
		stateNumber = "" + firstSymbol + firstNumber + secondNumber + thirdNumber + secondSymbol
				+ thirdSymbol + " " + REGION_NUMBER + " " + REGION_SYMBOL;
		
		if (!stateNumberSet.contains(stateNumber)) {			
			stateNumberSet.add(stateNumber);
			resp.setContentType("text/plain; charset=windows-1251");
			PrintWriter printWriter = resp.getWriter();
			printWriter.write(stateNumber);
			printWriter.close();			
			req.setAttribute("set", stateNumberSet);
			flag = true;
		}
		
		System.out.println(stateNumberSet);		
		
	}
	
	@GetMapping("/next")
	public void getNextNumber(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		if (stateNumberSet.isEmpty()) {
			resp.setContentType("text/plain; charset=windows-1251");
			PrintWriter printWriter = resp.getWriter();
			printWriter.write("The collection is empty. Use 'number/random'");
			printWriter.close();
		} else {
			if (flag == true) {
				numberInt = Integer.parseInt("" + firstNumber + secondNumber + thirdNumber);
				flag = false;
			}
			
			count = count + 1;
			//numberInt = numberInt + count;
			numberInt = numberInt + 1;
			
			if (numberInt == MAX_NUMBER) {
				numberString = "000";
				
				for (int i = 0; i < letters.length; i++) {
					if (firstSymbol == letters[i]) {
						firstSymbolIndex = i;
					}
					
					if (secondSymbol == letters[i]) {
						secondSymbolIndex = i;
					}
					
					if (thirdSymbol == letters[i]) {
						thirdSymbolIndex = i;
					}
				}
				
				if (thirdSymbolIndex == letters.length) {
					secondSymbol = letters[secondSymbolIndex + 1];
				} else if (thirdSymbolIndex == letters.length && secondSymbolIndex == letters.length) {
					firstSymbol = letters[firstSymbolIndex + 1];
				}
				
				thirdSymbol = letters[thirdSymbolIndex + 1];

				System.out.println(firstSymbolIndex);
				System.out.println(secondSymbolIndex);
				System.out.println(thirdSymbolIndex);
				
			} else {
				numberString = Integer.toString(numberInt);
				if (numberString.length() == 1) {
					numberString = "00" + numberString; 
				}
				
				if (numberString.length() == 2) {
					numberString = "0" + numberString; 
				}
			}
			
			stateNumber = "" + firstSymbol + numberString + secondSymbol + thirdSymbol
					+ " " + REGION_NUMBER + " " + REGION_SYMBOL;
			
			if (!stateNumberSet.contains(stateNumber)) {			
				stateNumberSet.add(stateNumber);
				resp.setContentType("text/plain; charset=windows-1251");
				PrintWriter printWriter = resp.getWriter();
				printWriter.write(stateNumber);
				printWriter.close();
			}
			
			numberInt = Integer.parseInt(numberString);
			
			
			
		}
		
		System.out.println(stateNumberSet);
		
	}
	
	public static int getRandomNumber(int[] array) {
	    int random = new Random().nextInt(array.length);
	    return array[random];
	}
	
	public static String getRandomSymbol(String[] array) {
	    int random = new Random().nextInt(array.length);
	    return array[random];
	}
	
}
