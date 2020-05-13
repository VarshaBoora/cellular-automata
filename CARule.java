package edu.neu.csye6200.ca;

enum RuleNames{	                  
	STASIS                              //rule name that the cell state does not changes	
}

public class CARule extends CACell{	

	private RuleNames ruleName;
	private int[] temp = new int[2];
	
	public RuleNames getRuleName() {
		return ruleName;
	}
	public void setRuleName(RuleNames ruleName) {
		this.ruleName = ruleName;
	}	
	public int[] getTemp() {
		return temp;
	}
	public void setTemp(int[] temp) {
		this.temp = temp;
	}
	
	public CARule() {}
	
	public CARule(RuleNames ruleName, CACrystal crystal, CACellState initCellState) {
		super(crystal, initCellState);
		this.ruleName = ruleName;
	}
	
	@Override
	public CACellState getNextCellState() {
		
		if (ruleName.equals(RuleNames.STASIS)) {
			return getStasis();
		}
		else {
			return getCaCellState();
		}
	}
	
	@Override
	public int[] getNextCellPos() {
		return getLockMePos(CACellState.DEAD);
	}
	
	public int[] getLockMePos(CACellState state) {
		try {
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (this.getCellRowPos() + i >= 0 && this.getCellRowPos() + i < this.getCrystal().getCrystalRows()
							&& this.getCellColPos() + j >= 0 && this.getCellColPos() + j < this.getCrystal().getCrystalCols()) {
						if (this.getCrystal().getCell(this.getCellRowPos() + i, this.getCellColPos() + j).getCaCellState()
								.compareTo(state) == 0) {
							if (!(i == 0 && j == 0)) 
							{
								temp[0] = this.getCellRowPos() + i; 
								temp[1] = this.getCellColPos() + j; 
								return temp;
							}
						}
					}
				}

			}			
			temp[0] = -1; 
			temp[1] = -1; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}	
	
	public int getAliveNeighbors(CACellState state) {
		
		int neighbours = 0;
		try {
			/*
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if(i!=0 && j!=0) {			
						if (this.getCellRowPos() + i >= 0 && this.getCellRowPos() + i < this.getCrystal().getCrystalRows()
								&& this.getCellColPos() + j >= 0 && this.getCellColPos() + j < this.getCrystal().getCrystalCols()) {
							
							if(this.getCellRowPos()%2 == 0) {
								if(this.getCrystal().getCellById(x-y-1)!=null && this.getCrystal().getCellById(x-y-1).getCaCellState().compareTo(state)==0) {
									neighbours++;
								}				
								if(this.getCrystal().getCellById(x+y-1)!=null && this.getCrystal().getCellById(x+y-1).getCaCellState().compareTo(state)==0) {
									neighbours++;
								}
							}else {
								
								if(this.getCrystal().getCellById(x-y+1)!=null && this.getCrystal().getCellById(x-y+1).getCaCellState().compareTo(state)==0) {
									neighbours++;
								}				
								if(this.getCrystal().getCellById(x+y+1)!=null && this.getCrystal().getCellById(x+y+1).getCaCellState().compareTo(state)==0) {
									neighbours++;
								}
							}
						}
					}
				}			
				
			}
			for (int i = 1; i >= -1; i--) {
				for (int j = -1; j <= 1; j++) {
					
					if (this.getCellRowPos() + i >= 0 && this.getCellRowPos() + i < this.getCrystal().getCrystalRows()
							&& this.getCellColPos() + j >= 0 && this.getCellColPos() + j < this.getCrystal().getCrystalCols()) {
						if(this.getCellRowPos()%2 == 0) {
							if(this.getCrystal().getCell(this.getCellRowPos() + i, this.getCellColPos() + j-1).getCaCellState()
									.compareTo(state)==0) {
								neighbours++;
							}
						}else {
							if(this.getCrystal().getCell(this.getCellRowPos() + i, this.getCellColPos() + j).getCaCellState()
									.compareTo(state)==0) {
								neighbours++;
							}
						}
					}
					
				}
			}
			int x= this.getCellID();
			int y= this.getCrystal().getCrystalRowCells()-1;
			int k=this.getCrystal().getCrystalTotalCells();
			
			if((x-y) >=1 && (x-y)<k) {
				if(this.getCrystal().getCellById(x-y).getCaCellState().compareTo(state)==0) {
					neighbours++;
				}
			}
			if((x-y+1) >=1 && (x-y+1)<k) {
				if(this.getCrystal().getCellById(x-y+1).getCaCellState().compareTo(state)==0)
					neighbours++;
			}
			if((x+1) >=1 && (x+1)<k) {
				if(this.getCrystal().getCellById(x+1).getCaCellState().compareTo(state)==0)
					neighbours++;
			}
			if((x-1) >=1 && (x-1)<k) {
				if(this.getCrystal().getCellById(x-1).getCaCellState().compareTo(state)==0)
					neighbours++;
			}
			if((x+y) >=1 && (x+y)<k) {
				if(this.getCrystal().getCellById(x+y).getCaCellState().compareTo(state)==0)
					neighbours++;
			}
			if((x+y+1) >=1 && (x+y+1)<k) {
				if(this.getCrystal().getCellById(x+y+1).getCaCellState().compareTo(state)==0)
					neighbours++;
			}*/
			//System.out.println("finding neighbors for this id: "+ this.getCellID());
			int x= this.getCellID();
			int y= this.getCrystal().getCrystalCols();
			//System.out.println("y is "+y);
			//System.out.println(this.getCellRowPos()%2);
			
			if(this.getCrystal().getCellStateById(x-y)!=null && this.getCrystal().getCellStateById(x-y).compareTo(state)==0) {
				neighbours++;
			}
			if(this.getCrystal().getCellStateById(x-1)!=null && this.getCrystal().getCellStateById(x-1).compareTo(state)==0) {
				neighbours++;
			}
			if(this.getCrystal().getCellStateById(x+1)!=null && this.getCrystal().getCellStateById(x+1).compareTo(state)==0) {
				neighbours++;
			}
			if(this.getCrystal().getCellStateById(x+y)!=null && this.getCrystal().getCellStateById(x+y).compareTo(state)==0) {
				neighbours++;
			}
			
			if(this.getCellRowPos()%2 == 0) {
				if(this.getCrystal().getCellStateById(x-y-1)!=null && this.getCrystal().getCellStateById(x-y-1).compareTo(state)==0) {
					neighbours++;
				}				
				if(this.getCrystal().getCellStateById(x+y-1)!=null && this.getCrystal().getCellStateById(x+y-1).compareTo(state)==0) {
					neighbours++;
				}
			}else {
				
				if(this.getCrystal().getCellStateById(x-y+1)!=null && this.getCrystal().getCellStateById(x-y+1).compareTo(state)==0) {
					neighbours++;
				}				
				if(this.getCrystal().getCellStateById(x+y+1)!=null && this.getCrystal().getCellStateById(x+y+1).compareTo(state)==0) {
					neighbours++;
				}
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			neighbours = 0;
		}
		return neighbours;
		
		/*int neighbours = 0;
		try {

			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (this.getCellRowPos() + i >= 0 && this.getCellRowPos() + i < this.getCrystal().getCrystalRows()
							&& this.getCellColPos() + j >= 0 && this.getCellColPos() + j < this.getCrystal().getCrystalCols()) {
						if (this.getCrystal().getCell(this.getCellRowPos() + i, this.getCellColPos() + j).getCaCellState()
								.compareTo(state) == 0) {
							if (!(i == 0 && j == 0)) 
								neighbours++;
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			neighbours = 0;
		}
		return neighbours;*/
		
	}
	
	public int getAliveNeighborsForTD(CACellState state) {
		
		int neighbours = 0;
		try {
			for (int j = -1; j <= 1; j++) {
				if (this.getCellRowPos() - 1 >= 0 && this.getCellRowPos() - 1 < this.getCrystal().getCrystalRows()
						&& this.getCellColPos() + j >= 0 && this.getCellColPos() + j < this.getCrystal().getCrystalCols()) {
					if (this.getCrystal().getCell(this.getCellRowPos() - 1, this.getCellColPos() + j).getCaCellState()
							.compareTo(state) == 0) {
						if (j != 0)
							neighbours++;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			neighbours = 0;
		}
		return neighbours;
	}

	private CACellState getStasis() {
		if(getCaCellState().equals(CACellState.DEAD) && (getAliveNeighbors(CACellState.ALIVE) == 1)) {
			return CACellState.ALIVE;			
		}
		
		else
			return getCaCellState();
	}
	
}
