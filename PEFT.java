

package mygraph;

import mygraph.Graph;
import java.util.Arrays;
import java.util.Comparator;



public class PEFT{

private int P, V;
private float[][] MatComm;
private float[][] MatExeProc;
private int[] scheduledList;
private Graph G;


	public PEFT(int V,int P,float[][] MatComm,float[][] MatExeProc,Graph G)
	{
		this.V = V;
		this.P = P;
		this.MatComm = MatComm;
		this.MatExeProc = MatExeProc;
		this.G = G;
                this.scheduledList = this.affectationTacheProc(this.readyList(this.octMatrix(MatExeProc, V, P)));
		
	}
        
        public int[] getScheduledList()
        {
            return this.scheduledList;
        
        }
        
        
	
	public int[] affectationTacheProc(int[] readyList)
	{
            int[] myArray = new int[this.V];
            
            for(int i=0;i<readyList.length;i++)
            {
                this.EFT(this.V,this.P);
                float[][] OEFT = this.OEFT(this.V,this.P);
                float min = OEFT[i][0];
                int proc = 0;
                for(int j=1;j<OEFT[i].length;j++)
                {
                        if(min>OEFT[i][j])
                        {
                                min=OEFT[i][j];
                                proc = j;
                        }
                }
                //j'affecte à la tache i le processeur j;
                myArray[i] = proc;

            }
            
            return myArray;
	}

	public float w(int t,int p)
	{
		return this.MatExeProc[t][p];

	}

	public float c(int i,int j)
	{

		return this.MatComm[i][j];

	}
	
	public  float OCT(int t,int p)
	{
		float max=0;
		//retourne 0 si la tache en cours corresponse à la tache de sortie
		if(t==(this.V-1)) return 0;
		if(this.G.adj[t].size()<=0) return 0;
		for(int v:this.G.adj(t))
		{

				float min = OCT(v,0) + this.w(v,0) + this.c(t,v);
				
				
				for(int proc=1; proc<this.P; proc++)
				{
					
					float tmp = OCT(v,proc) + this.w(v,proc) + this.c(t,v); 
					if(min>tmp)
						min = tmp;
					
				}
				
				if(max<=min)
					max = min;

		}
		
		return max;
		
	}
	
	public double rankOCT(int t)
	{
		float sum = 0;
		for(int i=0;i<this.P-1;i++)
		{
			sum = sum + this.OCT(t,i);
		}
		return sum/this.P;
	}
	
	public double[][] octMatrix(float[][] processorExeTime,int V, int P)
        {
	
            double[][] myArray= new double[V][P+1];
            for(int v=0; v<V; v++)
            {
                    for(int p=0; p<= P; p++)
                    {
                            if(p==P)
                                            myArray[v][p] = this.rankOCT(v);
                            else

                                    myArray[v][p] = processorExeTime[v][p];

                    }
            }
                    return myArray;
	}

//Definir la liste des taches suivant l'ordre croissant de la valeur du paramètre rankOCT	
	public int[] readyList(double[][] octMatrix)
	{
	int[]  myArray = new int[this.V];
	int tache; double max;
	
	//Definir la tache d'entrée comme première tache de la liste
	myArray[0] = 0;
		for(int k=1;k<this.V;k++)
		{
			max= octMatrix[k][octMatrix[k].length-1];
			tache = k;
			for(int i=k;i<octMatrix.length;i++)
			{
				

				if(max < octMatrix[i][octMatrix[i].length-1])
				{
						max = octMatrix[i][octMatrix[i].length-1];
						octMatrix[i][octMatrix[i].length-1] = -1;
						tache = i;
						
				}

			}
			myArray[k] = tache;
		}
		
		return myArray;
	}
	
        float[][] EFT(int V,int P)
	{
		float[][]  myArray = new float[V][P];
		for(int i=0;i<V;i++)
		{
			for(int j=0;j<P;j++)
			{
				 myArray[i][j] = this.EST(i,j) + this.MatExeProc[i][j];
				
			}
		}
		
		return myArray;
	}
	
	float[][] OEFT(int V,int P)
	{
		float[][]  myArray = new float[V][P];
		float[][] EFT = this.EFT(V,P);
		for(int i=0;i<V;i++)
		{
			for(int j=0;j<P;j++)
			{
				 myArray[i][j] =  EFT[i][j] + this.MatComm[i][j];
				
			}
		}
		
		return myArray;
	}	
	
	public  int EST(int v, int p)
	{
                if (v == 0) return 0;
		int max = 0;
		for(int w :this.G.pred(v))
		{
			int tmp = EST(w,p)+1;
			if(max < tmp) max = tmp;
			
		}
		
		return max;
	}	
	
	public int level(int t)
	{
		if (t == 0) return 1;
		int max = 0;
		for(int v :this.G.pred(t))
		{
			int tmp = level(v);
			if(max < tmp) max = tmp;
			
		}
		
		return max+1;
		
	}
}