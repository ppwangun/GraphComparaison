
import pandas as pd
import numpy
import matplotlib.pyplot as plt

col_list = ["node"]
noeud = pd.read_csv("scheduledTasks.csv", usecols=col_list,sep=';')

col_list = ["slrPeft"]
slrPeft = pd.read_csv("scheduledTasks.csv", usecols=col_list,sep=';')
noeud = noeud.to_numpy()
slrPeft = slrPeft.to_numpy()

plt.plot(noeud ,slrPeft,label="PEFT",c="green")

col_list = ["slrHeft"]
slrHeft = pd.read_csv("scheduledTasks.csv", usecols=col_list,sep=';')

slrHeft = slrHeft.to_numpy()

plt.plot(noeud ,slrHeft,label="HEFT",c="red")

plt.ylabel('SLR')

plt.xlabel('Nombre de noeud')

plt.legend()

plt.show()