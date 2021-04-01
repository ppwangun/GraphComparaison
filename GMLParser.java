import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import mygraph.Graph;
import mygraph.SET;
import java.util.*;

class Node{
    private Integer id;
    private String label;
    
    public Node(Integer id, String label)
    {
        this.id = id;
        this.label = label;
    }
    
    public Integer getId()
    {
        return this.id;
    }
    
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getLabel()
    {
        return this.label;
    }
    
    public void setLabel(String label)
    {
        this.label = label;
    }    
}



public class GMLParser{
    
    //Read file an return the conent as a string
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

    private static Float[][] costMatrix(Integer v,Integer proc,StringTokenizer dag)
    {
            Float [][] costMatrix = new Float[v][proc];
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
                                    costMatrix[countRow][countColumn] = Float.parseFloat(sb.toString());                                
                                countColumn = 0;
                                break;
                            }
                            else if(token[i]==',')
                            { 
                                
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Float.parseFloat(sb.toString());
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
                                    costMatrix[countRow][countColumn] = Float.parseFloat(sb.toString()); 
                                countColumn = 0;
                                break;
                            }
                            else if(token[i]==',')
                            {
                               
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Float.parseFloat(sb.toString()); 
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
                                    costMatrix[countRow][countColumn] = Float.parseFloat(sb.toString()); 
                                countColumn = 0;
                                break;
                            }
                            else if(token[i]==',')
                            {
                                
                                if(countRow<v && countColumn < proc )
                                    costMatrix[countRow][countColumn] = Float.parseFloat(sb.toString()); 
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

//retruns the list of edges having v as source
    public static ArrayList getVerticeEdge(int v,ArrayList<String> dag)
    {
            ArrayList<Integer> tab = new  ArrayList<Integer> ();
            for(String token : dag) 
            {
                if(token.equals("edge"))
                {
                    int pos = dag.indexOf(token);
                    int source = Integer.parseInt(dag.get(pos+2));
                    int target = Integer.parseInt( dag.get(pos+4));
                    if(v==source)
                        tab.add(target);

                } 
                
            }
            return tab;
    }
    public static void main(String args[])
    {
        //accept gml file name as argument
        String gmlFile = args[0];
        File myGmlFIle = new File(gmlFile);
        String content = null;

        try
        {
       
            content = readFile(myGmlFIle,Charset.forName("ISO-8859-1"));
            StringTokenizer st = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));
            
            int v = totalNode(st);
            StringTokenizer st1 = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));
            int p = totalProc(st1);
           
           System.out.println("Total tasks/nodes: "+v);
           System.out.println("Total processors: "+p);
           StringTokenizer st2 = new StringTokenizer(content.toString().trim().replaceAll("\\r|\\n", " "));
           System.out.println("yes "+costMatrix(v,p,st2)[3][5]);
           // content = readFile(myGmlFIle, StandardCharsets.UTF_8);
           char[] contents = content.toCharArray();
           Stack<Character> myStack = new Stack<Character>(); 
           Stack<StringBuffer> metaDataStack = new Stack<StringBuffer>();
           Stack<StringBuffer> dataStack = new Stack<StringBuffer>();
           
           ArrayList<StringBuffer> nodes = new ArrayList<StringBuffer>();
           ArrayList<StringBuffer> edges = new ArrayList<StringBuffer>();
           
           StringBuffer data = new StringBuffer();
           for(char car : contents)
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
           
           System.out.println("Node size : "+nodes.size()+" Edges size : "+edges.size());
           Graph myDag = parsedDag(nodes.size()+1,edges);
           System.out.print("My Parsed DAG IS : \n"+myDag);
            
        }
        catch(IOException ie)
        {
        }

        

    }
}