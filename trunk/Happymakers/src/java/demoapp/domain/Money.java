package demoapp.domain;

import java.io.Serializable;
import java.text.DecimalFormatSymbols;

public class Money implements Serializable {

	private int amount;

	public Money(String amount) throws NumberFormatException {
		int decPoint = amount.indexOf(DecimalFormatSymbols.getInstance().getDecimalSeparator());
		if (decPoint == -1) {
			this.amount = Integer.parseInt(amount) * 100;
		} else {
			String integerPart = amount.substring(0, decPoint);
			String decimalPart = amount.substring(decPoint +1);
			if (decimalPart.length() > 2) {
				throw new IllegalArgumentException("Only two decimals are allowed");
			}
			this.amount = Integer.parseInt(integerPart) * 100 + Integer.parseInt(decimalPart);
		}
	}

	public Money(int amount) {
		this.amount = amount;
	}

	public Money(int integerPart, int decimalPart) {
		if (decimalPart > 99) {
			throw new IllegalArgumentException("Only two decimals are allowed");
		}
		this.amount = integerPart * 100 + decimalPart;
	}

	public int getIntegerPart() {
		return amount / 100;
	}

	public int getDecimalPart() {
		return amount % 100;
	}

	public double getAsDouble() {
		return amount / 100.0d;
	}

	/**
	 * The two least significant digits are concidered to be the decimals.
	 * Thus, you can get the actual float value by dividing the integer
	 * returned by this method by 100.
	 */
	public int getFixedPointInteger() {
		return amount;
	}

	@Override
	public String toString() {
		return String.format("%d%c%02d", getIntegerPart(),
				DecimalFormatSymbols.getInstance().getDecimalSeparator(),
				getDecimalPart());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Money other = (Money) obj;
		if (this.amount != other.amount) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode() * 31 + this.amount;
	}
}
