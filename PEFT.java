

package mygraph;

import mygraph.Graph;
import java.util.Collections;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

class TaskProcessor{
    private int processor;
    private double AST;
    private double AFT;
    
    public TaskProcessor()
    {
        this.processor =-1;
        this.AST=-1;
        this.AFT=-1;
    
    }

    public int getProcessor() {
        return processor;
    }

    public void setProcessor(int processor) {
        this.processor = processor;
    }

    public double getAST() {
        return AST;
    }

    public void setAST(double AST) {
        this.AST = AST;
    }

    public double getAFT() {
        return AFT;
    }

    public void setAFT(double AFT) {
        this.AFT = AFT;
    }

    
}




public class PEFT{

private int P, V,entryNode,exitNode;
private double[][] MatComm;
private double[][] MatExeProc;

//Array of scheduled task
private TaskProcessor[] scheduledTask;
private Graph G;


	public PEFT(int V,int P,int entryNode,int exitNode,double[][] MatComm,double[][] MatExeProc,Graph G)
	{
		this.V = V;
		this.P = P;
		this.MatComm = MatComm;
		this.MatExeProc = MatExeProc;
		this.G = G;
                this.entryNode = entryNode;
                this.exitNode = exitNode;
                
                //initializing the array of shcduled tasks
                this.scheduledTask = new TaskProcessor[V];
                for(int i=0;i<V;i++)
                {
                    this.scheduledTask[i] = new TaskProcessor();
                    this.scheduledTask[i].setProcessor(-1);
                    this.scheduledTask[i].setAFT(0.0);
                    this.scheduledTask[i].setAFT(0.0);
                }                
		
	}
        
	/*public  float makespan()
	{
            HashMap<Integer,Float> makespan = new HashMap<Integer,Float>() ;
            ArrayList<Integer> readyList_1;
            double [] octMatrix;
            for(int level = 0;level <= this.level(this.V-1);level++)
            {
                    readyList_1 = this.readyList(this.V,level);
                    octMatrix = this.octMatrix(this.MatExeProc, this.V, this.P);
                    readyList_1 = this.sortReadyList(readyList_1,octMatrix);
                    this.levelMakespan(readyList_1,makespan);
            }
            Float  maxEntry = Collections.max(makespan.entrySet(), Map.Entry.comparingByValue()).getValue();
            return maxEntry;
	} */
        
       public double makespan()
        {
            int task = this.exitNode;
                     
           /* double min = 0.0;
            
            int proc=0;
                for(int j=0;j<this.P;j++)
                {
                    
                    double tmp =this.OEFT(task,j);
                    if(min<tmp)
                    {
                        
                        min=tmp;
                        proc = j;
                       
                    }
                    
                }*/

               return  this.scheduledTask[task].getAFT();
                     
        }

	public HashMap<Integer,Double> levelMakespan(ArrayList<Integer> readyList,HashMap<Integer,Double> myArray)
	{
   
            this.EFT(this.V,this.P);
            
            double levelMakespan = 0;
            double min = this.OEFT(0,0);
            int proc = 0;
            for(int i=0;i<readyList.size();i++)
            {
                Integer task = readyList.get(i);
                
                for(int j=0;j<this.P;j++)
                {
                    if(min>this.OEFT(task,j))
                    {
                        min=this.OEFT(task,j);
                        proc = j;
                    }
                }
                //j'affecte la tache i le processeur j;
                Double currentCost = myArray.get(proc);
                if(null != currentCost)
                    myArray.put(proc,min+currentCost);
                else myArray.put(proc,min);
            }
           
            //Object  maxEntry = Collections.max(myArray.entrySet(), Map.Entry.comparingByValue()).getKey();
            
            return myArray;
            
	}
        


	public double w(int t,int p)
	{
		return this.MatExeProc[t][p];

	}

	public double c(int i,int j)
	{

		return this.MatComm[i][j];

	}
	
	public  double OCT(int t,int p)
	{
            double max=0;
            
            double tmp;
            
            double  oct=0;
            //retourne 0 si la tache en cours corresponse à la tache de 
           
           // if(t==this.exitNode) return 0;
            
            for(int v : this.G.pred[this.exitNode])
            {
                if(v==this.exitNode) {oct=0; continue;}
                int proc = this.P-1;
                double min = oct + this.w(v,proc) + this.c(t,v);
                
                
                while(proc>0)
                { 
                    tmp = oct + this.w(v,proc) + this.c(t,v); 
                    
                    if(min>tmp)
                        min = tmp;
                    proc--;
                    
                }
                t = v;
                if(max<=min)
                   max = min;

            }
            
            return max;
		
	}
	
	public double rankOCT(int t)
	{
		double sum = 0;
		for(int i=0;i<this.P-1;i++)
		{
			sum = sum + this.OCT(t,i);
		}
              
		return sum/this.P;
	}
	
	public double[] octMatrix(double[][] processorExeTime,int V,int P)
        {
	  
            double[] myArray= new double[V];
            for(int v=0; v<V; v++)
            {
                myArray[v] = this.rankOCT(v);
  
            }
                return myArray;
	}

//Definir la liste des taches suivant l'ordre croissant de la valeur du paramètre rankOCT	
    public ArrayList<Integer> sortReadyList(ArrayList<Integer>readyList,double[]octMatrix)
    {
        ArrayList<Integer>  myArray =  new ArrayList<Integer>();
        int tache,tacheAux; double max;

        //Definir la tache d'entrée comme première tache de la liste
        
        for(int k=0;k<readyList.size();k++)
        {
            tache = readyList.get(k);
            max= octMatrix[tache];
            for(int i=0;i<readyList.size();i++)
            {
                //if (myArray.contains(i)) continue;
                if(max < octMatrix[readyList.get(i)])
                {
                    
                    max = octMatrix[readyList.get(i)];
                    //octMatrix[i][octMatrix[i].length-1] = -1;
                    tacheAux = readyList.get(i);
                    readyList.set(i,tache);
                    
                    
                    readyList.set(k,tacheAux);

                }

            }
            
           // readyList.set(k, tache);
            
            //octMatrix[tache] = -1;
           // myArray.set(k,tache);
        }
        

        return readyList;
    }
	
        double EFT(int i,int j)
	{

            return  this.findEST(i,j) + this.w(i,j);

	
	}
	
	double OEFT(int i,int j)
	{

                return  this.EFT(i,j) + this.OCT(i,j);
				
	
	}	
        
       
	
	
	public int level(int t)
	{
            if (t == this.entryNode) return 1;
            int max = 0;
            for(int v :this.G.pred(t))
            {
                    int tmp = level(v);
                    if(max < tmp) max = tmp;

            }

            return max+1;
		
	}

	//Traversing graph by level
	//collecting all tasks belonging to the given level
	
	private  ArrayList<Integer> readyList(int V,int level)
	{
		ArrayList<Integer> tasks  = new ArrayList<Integer>();
		for(int i=0;i<V;i++)
		{
                    if(this.level(i) == level)
                            tasks.add(i);
				
		}
		
		return tasks;
		
		
	}
        
        // Find EST
        private Double findEST(int task,int processor)
        {
            int i;
            Double ST,EST=-99999.0,commCost;
            for(i=0; i<this.V; i++)
            {
               // if(this.Data(i,task)!=-1)
                //{
                    // we consider that the communication cost between processors is equal to 0,
                    //Given that now days processors are at very high capacities
                    commCost = 0.0;
                   /* if(data_transfer_rate[schedule[i].processor][processor]==0)
                        comm_cost=0;
                    // Otherwise
                    else
                        comm_cost=this.Data(i,task)/this.[schedule[i].processor][processor];*/
                    
                    ST=this.scheduledTask[i].getAFT() + commCost;
                    // Try to find the max EST
                    if(EST<ST)
                        EST=ST;
               // }
            }
            return EST;
        } 
        
       public void makeSchedule()
       {
           
            HashMap<Integer,Float> makespan = new HashMap<Integer,Float>() ;
            ArrayList<Integer> readyList = new ArrayList<Integer>();
            double [] octMatrix;
            for(int level = 0;level <= this.level(this.V-1);level++)
            {
                    readyList = this.readyList(this.V,level);
                    octMatrix = this.octMatrix(this.MatExeProc, this.V, this.P);
                    readyList = this.sortReadyList(readyList,octMatrix);
                    
                       
                int task;
                TaskProcessor EST_EFT = new TaskProcessor(); 
                double tp = 0;
		for(int i=0;i<readyList.size();i++)
		{
                   
                    double min =1000000000,minEFT=1000000000;
                    int proc = 0;
                    task = readyList.get(i);
                    
                    //check if the task is the entry task
                    if(task == this.entryNode)
                    {
                        //Looking for the processor minimizing entryNode
                        for(int j=0;j<this.P;j++)
                        {
                            if(min>this.OEFT(task,j))
                            {
                                min=this.OEFT(task,j);
                                proc = j;
                            }
                        
                        }
                        this.scheduledTask[task].setAST(0);
                        this.scheduledTask[task].setAFT(this.MatExeProc[task][proc]);
                        this.scheduledTask[task].setProcessor(proc);
                    
                    }
                    else
                    {
                        TaskProcessor t = new TaskProcessor();;
                        for(int pro=0;pro<this.P;pro++)
                        {
                            tp = this.findEST(task,pro);
                           
                                if(minEFT>this.OEFT(task,pro))
                                {
                                     
                                    t.setAFT(tp+this.MatExeProc[task][pro]);
                                    t.setAST(tp);
                                   
                                    t.setProcessor(pro);
                                    proc=pro;
                                    
                                    minEFT = tp;
                                    min=tp;
                                }
                        }
                        this.scheduledTask[task] =t;
                    }
                    //j'affecte à la tache i le processeur j;
                    //System.out.println("task"+task+" affected to processor "+proc+" AFT = "+min);
                    

		}           
            }
       
       }
       
       double CPmin()
       {
            double min = 999999;
            double cpmin = 0.0;
            double levelMin = 9999999;
            ArrayList<Integer> readyList = new ArrayList<Integer>();
            double [] octMatrix;
            for(int level = 0;level <= this.level(this.V-1);level++)
            {
                    readyList = this.readyList(this.V,level);

                for(int v=0 ;v<readyList.size();v++)
                {
                    for(int proc=0;proc<this.P;proc++)
                    {
                        if(min>this.w(readyList.get(v), proc))
                        {
                            min = this.w(readyList.get(v), proc);

                        }
                    }
                   if ( levelMin >=min) levelMin=min;
                }
                cpmin+=levelMin;
           
           }
            return cpmin;
       }
       
       public double slr()
       {
           return this.makespan()/this.CPmin();
       }
       
       public double speedup()
       {
           
           double sum=0.0;
           for(int v=0;v<this.V;v++)
           {
               double min = 999999;
                for(int proc=0;proc<this.P;proc++)
                {
                    if(min>this.w(v, proc))
                    {
                        min = this.w(v, proc);

                    }
                }  
                
                sum+=min;
           }
           
           return sum/this.makespan();
       
       }
       
    public double exeTime()
       {
            double min = 999999;
            double exeTime = 0.0;
            double levelMax = -9999999;
            ArrayList<Integer> readyList = new ArrayList<Integer>();
            double [] octMatrix;
            for(int level = 0;level <= this.level(this.V-1);level++)
            {
                    readyList = this.readyList(this.V,level);

                for(int v=0 ;v<readyList.size();v++)
                {
                   if ( levelMax <this.scheduledTask[readyList.get(v)].getAFT()) levelMax=this.scheduledTask[readyList.get(v)].getAFT();
                }
                exeTime+=levelMax;
           
           }
            return exeTime;
       }        
        
       
}