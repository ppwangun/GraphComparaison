import matplotlib.pyplot as plt


hematies = [4.51, 4.92, 4.66, 4.63, 4.67, 4.72, 4.76, 4.76, 4.57, 4.58, 4.46, 4.63, 4.59, 4.46] # nb de globules rouges

vgm = [94.0, 93.0, 93.0, 93.4, 93.4, 94.4, 93.6, 94.3, 94.9, 94.9, 96.8, 95.2, 95.0, 97.2] # taille des globules rouges
plt.plot(hematies,vgm,"ob") # ob = type de points "o" ronds, "b" bleus

plt.show() # Obligatoire pour voir le graphique

