
package mygraph.PEFT;

import mygraph.Graph;


public class PEFT{

private int P, V;
private int[][] MatComm;
private int[][] MatExeProc;
private Graph G;

	public PEFT(int V,int P,int[][] MatComm,int[][] MatExeProc,Graph G)
	{
		this.V = V;
		this.P = P;
		this.MatComm = MatComm;
		this.MatExeProc = MatExeProc;
		this.Graph = G;
	}
	
	

	private int c(int t,int p)
	{

		for(int v=0; v< this.MatExeProc.length; v++)
		{
			
			for(int w=0; w<this.MatExeProc[v].length ;w++)
			{
				if ((v == t)&(w==p))
					return MatExeProc[v][w];
			}
		}
	}

	private int c(int i,int j)
	{
		for(int v=0; v< this.MatComm.length; v++)
			
			for(int w=0; w<this.MatComm[v].length ;w++)
			
				if ((v == t)&(w==p))
					return MatExeProcMatComm;
		
	}
	
	public  int OCT(int t,int p)
	{
		int max=0;
		for(int v:this.G.adj(t))
		{
		
			int min = 0;
			if(v==P-1) return 0;
			for(int w=0; w<P; p++)
			{
				int tmp = OCT(v,w,G,P) + this.w(v,w) + this.c(t,v);
				if(min>tmp)
					min = tmp;
			}
			if(max<= min)
				max = min;
		
		}
		
		return max;
		
	}
}