
//package mygraph;

import mygraph.Graph;
import java.util.Arrays;
import java.util.Comparator;



public class HEFT{

private int P, V;
private int[][] MatComm;
private int[][] MatExeProc;
private Graph G;
private int[][] EST;
private int[] affectationTacheProc;
private int entryNode;
private int exitNode;

	public HEFT(int V,int P,int entryNode,int exitNode,int[][] MatComm,int[][] MatExeProc,Graph G)
	{
		this.V = V;
		this.P = P;
		this.MatComm = MatComm;
		this.MatExeProc = MatExeProc;
		this.G = G;
		this.EST = EST;
		this.affectationTacheProc = new int[this.V];
                this.entryNode = entryNode;
                this.exitNode = exitNode;
	}
	private int avail(int j)
	{
		
		int exeTime =0;
		//searching last task assigned to proc j
		for(int v=0;v<this.V;v++)
		{
	
				if(j==this.affectationTacheProc[v]) 
				{	//serching exucting tile for lastTask
		
					exeTime += this.MatExeProc[v][j];
		
				}
		}
		
		return exeTime;
		//if no task is assigned to proc j retun 0
	
	}

	public int[] affectationTacheProc(int[] readyList)
	{
		int[] myArray = new int[V];
		for(int i=0;i<readyList.length;i++)
		{

				
				int[][] EFT = this.EFT(this.V,this.P);
				int min = EFT[i][0];
				int proc = 0;
				for(int j=1;j<EFT[i].length;j++)
				{
					if(min>EFT[i][j])
					{
						min=EFT[i][j];
						proc = j;
					}
				}
				//j'affecte à la tache i le processeur j;
				myArray[i] = proc;

		}
		return myArray;
	}

	double C(int i,int k)
	{
			//check the processor to which i is affectedTaskToProc
			int m = this.affectationTacheProc[i];
			int n = this.affectationTacheProc[i];
			
			//start time of processor Pm
			
			int Lm = this.L(this.P)[m];
			
			return Lm + (this.Data(i,k)/this.B(m,n));
	}
	private int Data (int i, int k)
	{
		int[][] myArray= MatComm;
		for(int v=0; v<this.P; v++)
		{
			
			for(int w=0;w<this.P; w++)
			{
				if ((i==v) && (w==k))
					return myArray[v][w];
			}
		}
		
		return 0;			
	}
	private int[][] B()
	{
		int[][] myArray= new int[this.P][this.P];
		int p = this.P;
		for(int v=0; v<p; v++)
		{
			
			for(int w=0;v<p; w++)
			{
				if(v!=w)
					myArray[v][w] = 1;
				else myArray[v][w] = 0;
			}
		}
		
		return myArray;		
	}
	//Communication cost for tranfering data from processor Pm to processor Pn
	private int B(int m,int n)
	{
		int p = this.P;
		
		for(int v=0; v<p; v++)
		{
			
			for(int w=0;v<p; w++)
			{
				if(v==m && w ==n)
					return this.B()[v][w];
			}
		}
			return 0;
	}
	
	private double avgB (int[][] B)
	{
		int s = 0;
		int p = this.P;
		for(int v=0; v<p; v++)
		{
			
			for(int w=0;w<p; w++)
			{
				s = s+B[v][w];
			}
		}
		
		return s/(p*p);
	}
	
	private int[] L(int p)
	{
		int[] myArray = new int[p];
		for(int v=0;v<p; v++)
		{
			
				myArray[v] = 0;
			
		}
		return myArray;		
	}
	
	private double avgL(int[] L)
	{
		int s=0;
		for(int v=0;v<this.P; v++)
		{
			
			s=s+L[v];
			
		}
		return s/this.P;		
	}
	
	
	public int avgW(int i)
	{
		int s = 0;
		int q = this.V;
		for(int j=1;j<q;j++)
			s = s + this.w(i,j)/q;
		
		return s;

	}

	private int w(int i,int j)
	{

		return this.MatExeProc[i][j];

	}
	
	private double avgC(int i, int k)
	{
		int p = this.P;
		double L = this.avgL(this.L(p));
		double B = this.avgB(this.B());
		
		return  this.Data(i,k)/B ;
		
		
	}
	

	
	public double ranKu(int t)
	{
		double s = this.avgW(t);
		if(t==this.V-1) return this.avgW(t);
		double max = 0;
		for(int v:this.G.adj(t))
		{
			double m = (this.avgC(t,v)+ranKu(v));
			
			if (max < m) max = m;
			
		}
		
		return this.avgW(t)+max;
		
	}


//Definir la liste des taches suivant l'ordre croissant de la valeur du paramètre rankOCT	
	public int[] schedulingList()
	{
	double[]  myArray = new double[this.V];
	int[]  myArray1 = new int[this.V];
	int task=0; double max;
	
	//Compute ranku for each task;
		for(int k=0;k<this.V;k++)
		{
			myArray[k]=this.ranKu(k);

		}
		
		//sorting tasks based on descending order of ranku
		for(int i=0;i<this.V;i++)
		{
		  max=0;
			
			for(int j=0;j<this.V;j++)
			{
				if(max<myArray[j])
				{
					max = myArray[j];
					task = j;
				}
			}
			myArray1[i] = task;

		}		
		return myArray1;
	}
	
	int[][] EFT(int V,int P)
	{
		int[][]  myArray = new int[V][P];
		for(int i=0;i<V;i++)
		{
			for(int j=0;j<P;j++)
			{
				 myArray[i][j] = this.EST[i][j] + this.w(i,j);
				
			}
		}
		
		return myArray;
	}
	
	
	
	private  double EST(int i, int j)
	{
		//in cas i == entry task
		if(i==this.entryNode) return 0;
		double max = 0;
		for (int w : this.G.pred(i)) {
			if(this.affectationTacheProc[w]==j)
				 max = (this.w(w,j)+this.C(w,i) +EST(w,j)) ;
			else max = this.C(w,i);
			if(max < this.avail(j) ) return max;
		}
		
		return max;
	}	
	

}