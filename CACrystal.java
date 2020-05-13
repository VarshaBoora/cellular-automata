package edu.neu.csye6200.ca;

import java.awt.Polygon;
import java.util.ArrayList;

public class CACrystal {
	
	private int crystalRows, crystalCols;
	private RuleNames ruleNames;
	private CACell[][] arrCells;
	private ArrayList<CACell> list;
	private int initAliveCell;		
	private int activeCellRowpos = 0;
	private int activeCellColpos = 0;
	private boolean isLocked;
	private int crystalTotalCells, crystalRowCells;
	
	
	public ArrayList<CACell> getList() {
		return list;
	}

	public void setList(ArrayList<CACell> list) {
		this.list = list;
	}

	public CACrystal() {}	
	
	public CACrystal(int crystalRows, int crystalCols, RuleNames ruleNames, int initAliveCell) {
		super();
		this.crystalRows = crystalRows;
		this.crystalCols = crystalCols;
		this.ruleNames = ruleNames;
		this.setInitAliveCell(initAliveCell);
		this.activeCellRowpos = 0;
		this.activeCellColpos = 0;
		this.isLocked = false;
		this.arrCells = new CACell[crystalRows][crystalCols];
		this.list = new ArrayList<CACell>();
		crystalInitialize();
	}
	
	public void crystalInitialize() {
		final int[] xs = new int[6];
        final int[] ys = new int[6];
        int id=0;
		for(int i=0; i<crystalRows ; i++) {
			for(int j=0 ; j<crystalCols; j++) {	
				id++;
				CACell ca = new CARule(this.ruleNames, this, CACellState.DEAD);
				ca.setCellRowPos(i);
				ca.setCellColPos(j);
				ca.setCrystal(this);
				ca.setSideLength(7);
				final int[] k = {0};
				ca.foreachVertex((x, y) -> {
	                  xs[k[0]] = (int)((double)x);
	                  ys[k[0]] = (int)((double)y);
	                  k[0]++;
	                });
				ca.setPoly(new Polygon(xs , ys, 6));
				ca.setCellID(id);
				this.arrCells[i][j] = ca;
				this.list.add(ca);
			}		
	    }
		this.setCrystalTotalCells(id);
		System.out.println(this.list.get(0).getCellID());
	}
	
	public CACrystal(CACrystal prevCrystal) {
		ruleNames = prevCrystal.ruleNames;
		crystalRows = prevCrystal.crystalRows;
		crystalCols = prevCrystal.crystalCols;
		arrCells = new CACell[crystalRows][crystalCols];
		list = new ArrayList<CACell>();
		isLocked = prevCrystal.isLocked;
		activeCellRowpos = prevCrystal.activeCellRowpos;
		activeCellColpos = prevCrystal.activeCellColpos;
		
		crystalInitialize();
	}

	public CACrystal createNextCrystal() {
		
		CACrystal newCrystal = new CACrystal(this);
		CACellState[][] newCellStates;

		try {
			 
				newCellStates = nextCellStates();
				for (int i = 0; i < getCrystalRows(); i++) {
					for (int j = 0; j < getCrystalCols(); j++) {
						newCrystal.getCell(i, j).setState(newCellStates[i][j]);
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newCrystal;
	}
	
	public CACellState[][] nextCellStates() {
		CACellState[][] nextStates = new CACellState[getCrystalRows()][getCrystalCols()];

		for (int i = 0; i < getCrystalRows(); i++) {
			for (int j = 0; j < getCrystalCols(); j++) {
				nextStates[i][j] = getCell(i, j).getNextCellState();
			}
		}
		return nextStates;
	}

	public CACell getCell(int row, int col) {
		if((row < 0) || (row >= getCrystalRows())) {
			throw new RuntimeException("The referenced cell at " + row + "is not valid row in the current crystal.");
		}
		if ((col < 0) || (col >= getCrystalCols())) {
			throw new RuntimeException("The referenced cell at " + col + "is not valid column in the current crystal.");
		}
		return arrCells[row][col];
	}
	
	public CACellState getCellStateById(int id) {
		
		if(id >= 1 && id<= list.size())
			return list.get(id-1).getCaCellState();
		else
			return CACellState.UNDEFINED;
		
	}
	
	public int[] nextActivePos() {
		return getCell(getActiveCellRowpos(), getActiveCellColpos()).getNextCellPos();
	}
	

	public int getCrystalTotalCells() {
		return crystalTotalCells;
	}

	public void setCrystalTotalCells(int crystalTotalCells) {
		this.crystalTotalCells = crystalTotalCells;
	}

	public int getCrystalRowCells() {
		return crystalRowCells;
	}

	public void setCrystalRowCells(int crystalRowCells) {
		this.crystalRowCells = crystalRowCells;
	}

	public int getActiveCellRowpos() {
		return activeCellRowpos;
	}

	public void setActiveCellRowpos(int activeCellRowpos) {
		this.activeCellRowpos = activeCellRowpos;
	}

	public int getActiveCellColpos() {
		return activeCellColpos;
	}

	public void setActiveCellColpos(int activeCellColpos) {
		this.activeCellColpos = activeCellColpos;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public RuleNames getRuleNames() {
		return ruleNames;
	}

	public void setRuleNames(RuleNames ruleNames) {
		this.ruleNames = ruleNames;
	}

	public CACell[][] getArrCells() {
		return arrCells;
	}

	public void setArrCells(CACell[][] arrCells) {
		this.arrCells = arrCells;
	}

	public int getInitAliveCell() {
		return initAliveCell;
	}

	public void setInitAliveCell(int initAliveCell) {
		this.initAliveCell = initAliveCell;
	}

	public int getCrystalRows() {
		return crystalRows;
	}

	public void setCrystalRows(int crystalRows) {
		this.crystalRows = crystalRows;
	}

	public int getCrystalCols() {
		return crystalCols;
	}

	public void setCrystalCols(int crystalCols) {
		this.crystalCols = crystalCols;
	}
}
