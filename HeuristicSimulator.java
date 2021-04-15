import 

public class HeuristicSimulator{
	
	
	//Traversing graph by level
	//collecting all tasks belonging to the given level
	
	private static ArrayList<int> levelTasks(int V,int level)
	{
		ArrayList<int> tasks  = new ArrayList();
		for(int i=0;i<V;i++)
		{
			if(level(i) == level
				tasks.add(i);
				
		}
		
		return tasks;
		
		
	}
	
	
	//COmpute the level of a task
	public static int level(int t)
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