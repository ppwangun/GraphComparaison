import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import mygraph.Graph;
import mygraph.SET;
import mygraph.PEFT;
import mygraph.HEFT;
import mygraph.Stack;
import java.util.*;




public class GMLParser{
    
    //Read gml file an return the conent as a string
    public static String readFile(File file, Charset encoding) throws IOException {
       
        FileReader fr = new FileReader(file);
        char[] chars  = new char[(int)file.length()];
        int offset = 0;
            while (offset < chars.length) {
                int result = fr.read(chars, offset, chars.length - offset);
                if (result == -1) {
                    break;
                }
                offset += result;
            }
            return new String(chars);

    }
    
    //compute the number of nodes of the graph
    private static int totalNode(StringTokenizer dag)
    {
        int countNode = 0;
        while(dag.hasMoreTokens() )
        {
            if(dag.nextToken().equals("node"))
            {
               countNode++; 
            }
        } 
        return countNode;
        
    }
    
    //extract the number of processors from the GML DAG
    private static int totalProc(StringTokenizer dag)
    {
        int nbProc = 0;
        while(dag.hasMoreTokens() )
        {
            String token = dag.nextToken();
            if(token.equals("nbproc"))
            {
               nbProc = Integer.parseInt(dag.nextToken());
              
            }
        } 
        return nbProc;
        
    }    

    //Extract the costmatrix from the gml DAG
    private static double[][] costMatrix(Integer v,Integer proc,StringTokenizer dag)
    {
            double [][] costMatrix = new double[v][proc];
            char[] token;
            
            int countRow = 0;
            int countColumn = 0;
        
        while(dag.hasMoreTokens() )
        {
 
            if(dag.nextToken().equals("costmatrix"))
            {
                
                while(dag.hasMoreTokens() )
                {
                    token = dag.nextToken().toCharArray();
                    StringBuffer sb = new StringBuffer();
                    if(token[0]=='"' && token[1]=='[')
                    {

                        for(int i=3;i<=token.length-1;i++)
                        {
                            if(token[i]==']')
                            {
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Double.parseDouble(sb.toString());                                
                                countColumn = 0;
                                break;
                            }
                            else if(token[i]==',')
                            { 
                                
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Double.parseDouble(sb.toString());
                                countColumn++;
                                break;
                            }
                            else sb.append(token[i]);
                        }

                        
                    }
                    else if(token[0]=='[')
                    {
                        countRow++;
                        countColumn = 0;
                        for(int i=1;i<=token.length-1;i++)
                        {
                            if(token[i]==']')
                            {
                                
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Double.parseDouble(sb.toString()); 
                                countColumn = 0;
                                break;
                            }
                            else if(token[i]==',')
                            {
                               
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Double.parseDouble(sb.toString()); 
                                countColumn++;
                                break;
                            }
                            else sb.append(token[i]);
                        }

                        
                    }
                    else
                    {
                        for(int i=0;i<=token.length-1;i++)
                        {
                            if(token[i]==']')
                            {
                               
                                
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Double.parseDouble(sb.toString()); 
                                countColumn = 0;
                                break;
                            }
                            else if(token[i]==',')
                            {
                                
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Double.parseDouble(sb.toString()); 
                                countColumn++;
                                break;
                            }
                            else sb.append(token[i]);
                        }

                     
                    }                    
                }
                  break;  
            }
        }
        return costMatrix;    
       
    }
    
    private static double costTask(int t,int p,double[][] costMatrix)
    {
        return costMatrix[t][p];
    
    }
    
    public static void writeToCSV(Path file,StringBuffer line) throws IOException
    {
 
       // String header = "SLR HEFT,SLR PEFT,Temps execution";
        ///StringBuffer sb = new StringBuffer(line+System.lineSeparator());
        //Files.write(file, header.getBytes(),StandardOpenOption.APPEND);
        

		//String line = param1+","+param2+","+param3+System.lineSeparator();
             
                //sb.append(System.lineSeparator());
                //sb.append(line);
                
                //Files.write(file, text.getBytes(), StandardOpenOption.APPEND);
              
		
	
        
        Files.write(file, line.toString().getBytes(), StandardOpenOption.APPEND);
        /*FileWriter fw = new FileWriter("C:\\TP\\My_Project\\GraphComparaison\\scheduledTasks.csv", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw); 
        pw.println("Shane"); pw.println("Root"); pw.println("Ben");


        pw.write("yeah man");
        
        pw.flush();
        pw.close();
                bw.close();
                fw.close();*/
          
   
    }

    
    //Retrieve the communication matrix
    
    public static double[][] commMatrix(Integer V, ArrayList<StringBuffer> edges) 
    {

        double[][] commMat = new double[V][V];
        //Initializing the matircie to 0
        for(int v=0;v<=V-1;v++)
             for(int w=0;w<=V-1;w++)
                 commMat[v][w]=0.00000000f;

        for(StringBuffer edge: edges)
        {
            String edge1 = edge.toString().replaceAll("\\s{2,}", " ").trim();

            String[] tokens = edge1.split(" ");

               // String token = st.nextToken();

                int v = Integer.parseInt(tokens[1]);
                int w = Integer.parseInt(tokens[3]);
                commMat[v][w] = Double.parseDouble(tokens[5]);
        }
      
        return commMat;
    }    

    //Retrive eges from GML file
    
    public static ArrayList<StringBuffer> collectEdges(char[] fileContent)
    {
           Stack<Character> myStack = new Stack<Character>(); 
           Stack<StringBuffer> metaDataStack = new Stack<StringBuffer>();
           Stack<StringBuffer> dataStack = new Stack<StringBuffer>();
           
           ArrayList<StringBuffer> nodes = new ArrayList<StringBuffer>();
           ArrayList<StringBuffer> edges = new ArrayList<StringBuffer>();
           
           StringBuffer data = new StringBuffer();
           for(char car : fileContent)
           {
               
               
               if(car == '[')
               {
                   metaDataStack.push(data);
                   data = new StringBuffer();
                   
                   
               }
               else if(car == ']')
               {
                   StringBuffer metaData = metaDataStack.pop();
                   //System.out.println("yest-"+metaData.toString().trim());
                   if(metaData.toString().trim().equals("node"))
                   {
                        String text = data.toString().trim().replaceAll("\\r|\\n", "");
                        data = new StringBuffer(text);
                         dataStack.push(data);
                         nodes.add(data);
                   }
                   if(metaData.toString().trim().equals("edge"))
                   {
                        String text = data.toString().trim().replaceAll("\\r|\\n", "");
                        data = new StringBuffer(text);
                         dataStack.push(data);
                         edges.add(data);
                   }                   
                   data = new StringBuffer();
               }
               else 
               {
                   if(car!='\t')
                    data.append(car);

               }
           } 
           return edges;
        
    }

    /**
     * Returns a random simple graph containing {@code V} vertices and {@code E} edges.
     * @param V the number of vertices
     * @return a random simple graph on {@code V} vertices, containing a total
     *     of {@code E} edges
     * 
     */
    public static Graph parsedDag(int V, ArrayList<StringBuffer> edges) {

            Graph G = new Graph(V);
            //Integer v =0;
            int pos = 0;
            
            for(StringBuffer edge: edges)
            {
                String edge1 = edge.toString().replaceAll("\\s{2,}", " ").trim();
                
                String[] tokens = edge1.split(" ");

                   // String token = st.nextToken();
                    
                    int v = Integer.parseInt(tokens[1]);
                    int w = Integer.parseInt(tokens[3]);
                    G.addEdge(v,w);

                
            }
 
           // int w = StdRandom.uniform(V);
            //Edge e = new Edge(v, w);
            //if ((v != w) && v!=V-1 && !set.contains(e) && w!=0) {
            //set.add(e);
           /* ArrayList<Integer> targetList = getVerticeEdge(v,dag);
            for(Integer item: targetList)
                G.addEdge(v,item);
            */
      
        return G;
    }
    
        private static int entryNode(Graph G,int V)
        {
            int entry = 0;
            for(int i=0;i<V;i++)
            {
                if(
                        G.pred[i].size()==0)
                        return i;

            }
            return entry;
        }
        
        private static int exitNode(Graph G,int V)
        {
            int exit = 0;
            for(int i=0;i<V;i++)
            {
                if(G.adj[i].size()==0)
                        return i;

            }
            return exit;
        }     


    public static void main(String args[])
    {
        //accept gml file name as argument

        try
        {
            String header = "node;slrPeft;speedupPeft;exeTimePeft;slrHeft;speedupHeft,exetimeHeft";
            StringBuffer sb = new StringBuffer(header+System.lineSeparator());
            Path path = Paths.get("scheduledTasks.csv");            
            for(int i=0;i<args.length;i++)
            { System.out.println("length arguments"+args[i]);
                String gmlFile = args[i];
                File myGmlFIle = new File(gmlFile);
                String content = null;
                double[][] costMatrix;

                content = readFile(myGmlFIle,Charset.forName("ISO-8859-1"));
                StringTokenizer st = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));

                int v = totalNode(st);
                StringTokenizer st1 = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));
                int p = totalProc(st1);

               System.out.println("Total tasks/nodes: "+v);
               System.out.println("Total processors: "+p);
               StringTokenizer st2 = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));

               // content = readFile(myGmlFIle, StandardCharsets.UTF_8);
               char[] contents = content.toCharArray();

               ArrayList<StringBuffer> edges = collectEdges(contents);

               Graph myDag = parsedDag(v,edges);
               //System.out.print("My Parsed DAG IS : \n"+myDag);

               StringTokenizer dag = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));
               StringTokenizer dag1 = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));
               costMatrix = costMatrix(v,p,dag);

               int entryNode = entryNode(myDag,v);
               int exitNode = exitNode(myDag,v);
               System.out.print("Entry node is  : "+entryNode+"\n");
               System.out.print("Exit node is  : "+exitNode+"\n");


               HEFT heft = new HEFT(v,p,entryNode,exitNode,commMatrix(v,edges),costMatrix,myDag);


               int[] heftScheduledList = heft.schedulingList();
               heft.makeSchedule(heftScheduledList );
               double slrHeft = heft.slr();
               double speedupHeft = heft.speedup();
               double exeTimeHeft = heft.exeTime();

               




              // System.out.println("yes yes   ....."+peftScheduledList.length);
              // writeToCSV(peftScheduledList,path,costMatrix);

              //System.out.println("le MAKESPAN DE l'agorithme HEFT  "+mkspanHeft);

               PEFT peft = new PEFT(v,p,entryNode,exitNode,commMatrix(v,edges),costMatrix,myDag);
               //int[] peftScheduledList = peft.schedulingList();
                peft.makeSchedule();
                double slrPeft = peft.slr();
                double speedupPeft = peft.speedup();
                double exeTimePeft = peft.exeTime();
                double exeTime = 0.0;


              
                System.out.println("le MAKESPAN DE l'agorithme PEFT  "+speedupPeft);
                System.out.println("le MAKESPAN DE l'agorithme HEFT  "+speedupHeft);
                
                 String line = v+";"+slrPeft+";"+speedupPeft+";"+exeTimePeft+";"+slrHeft+";"+speedupHeft+";"+exeTimeHeft+System.lineSeparator();
                sb =  sb.append(line);

            }
            
            Files.createFile(path);
            writeToCSV(path,sb);
        

        }
        catch(IOException ie)
        {
            System.out.println(ie);
            
        }        

    }
}