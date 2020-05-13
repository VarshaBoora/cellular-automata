package edu.neu.csye6200.ca;

import java.awt.Polygon;
import java.util.function.BiConsumer;

enum CACellState{
	ALIVE, DEAD, UNDEFINED ; 
}

public abstract class CACell{		
	
	private CACellState caCellState;
	private CACrystal crystal;
	private int cellRowPos, cellColPos;
	private static int cellCount = 0;
	private int sideLength;
	private static final double H = Math.sqrt(3) / 2;
	private Polygon poly;
	private int cellID;
	
	public int getCellID() {
		return cellID;
	}

	public void setCellID(int cellID) {
		this.cellID = cellID;
	}

	public Polygon getPoly() {
		return poly;
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	public static double getH() {
		return H;
	}

	public int getSideLength() {
		return sideLength;
	}

	public void setSideLength(int sideLength) {
		this.sideLength = sideLength;
	}

	public static int getCellCount() {
		return cellCount;
	}

	public static void setCellCount(int cellCount) {
		CACell.cellCount = cellCount;
	}

	public CACrystal getCrystal() {
		return crystal;
	}

	public void setCrystal(CACrystal crystal) {
		this.crystal = crystal;
	}

	public int getCellRowPos() {
		return cellRowPos;
	}

	public void setCellRowPos(int cellRowPos) {
		this.cellRowPos = cellRowPos;
	}

	public int getCellColPos() {
		return cellColPos;
	}

	public void setCellColPos(int cellColPos) {
		this.cellColPos = cellColPos;
	}

	public CACellState getCaCellState() {
		return caCellState;
	}

	public void setCaCellState(CACellState caCellState) {
		this.caCellState = caCellState;
	}	
	
	public CACell() {}
	
	public CACell(CACrystal crystal, CACellState cellState) {
		this.crystal = crystal;		
		
			if (cellCount+1 == crystal.getInitAliveCell()) {
				this.caCellState = CACellState.ALIVE;				
			}
			else
				this.caCellState = cellState;
		
		cellCount++;
	}
	
	double getCenterX() {
        return 2 * H * sideLength * (cellColPos + (cellRowPos % 2) * 0.5);
      }

      double getCenterY() {
        return 3 * sideLength / 2  * cellRowPos;
      }

      void foreachVertex(BiConsumer<Double, Double> f) {
        double cx = getCenterX();
        double cy = getCenterY();
        f.accept(cx + 0, cy + sideLength);
        f.accept(cx - H * sideLength, cy + 0.5 * sideLength);
        f.accept(cx - H * sideLength, cy - 0.5 * sideLength);
        f.accept(cx + 0, cy - sideLength);
        f.accept(cx + H * sideLength, cy - 0.5 * sideLength);
        f.accept(cx + H * sideLength, cy + 0.5 * sideLength);
      }
    
	
	public void setState(CACellState state) {
		if(!caCellState.equals(state))
			caCellState = state;
	}
	
	public abstract CACellState getNextCellState();
	
	public abstract int[] getNextCellPos();
}
