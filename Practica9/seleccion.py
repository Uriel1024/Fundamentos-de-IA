#Importamos todas las librerias para poder 
import joblib
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.feature_selection import RFE
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, roc_auc_score, confusion_matrix, ConfusionMatrixDisplay

archivo = input('Dime el nombre del archivo CSV: ')

file_name = archivo  + ".csv"

dataframe  = pd.read_csv(file_name)

x = dataframe.iloc[:,:-1]

y = dataframe.iloc[:, -1]

maximo = X.shape[1]

modelo = RandomForestClassifier(random_state=42) 

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)


print("\nTrabajando en la eleccion...")

best_features = pd.Index([])
score = []
values = []
n_features = 0

roc = 0 

for i in range (2, maximo + 1):
	selector_caracteristicas = RFE(modelo, n_features_to_select = i, step  =1)

	selector_caracteristicas = selector_caracteristicas.fit(X_train, y_train)

	selected_features = X.columns[selector_caracteristicas.support_]

	 X_new = dataframe.loc[:, selected_features]

	 X_ntrain, X_ntest, y_train, y_test = train_test_split(X_new, y, test_size = 0.2, random_state= 42)

	 modelo.fit(X_ntrain,y_train)

	 y_pred = modelo.predict(X_ntest)

	 roc_auc = roc_auc_score(y_test, y_pred)

	 scores.append(roc_auc)
	 values.append(i)


tittle = f"Seleccion de características usando RFE ({file_name})"
plt.figure(figsize = (10,6))
plt.plot(values,scores,marker = 'o')
plt.xlabel('Numero de características seleccionadas')
plt.ylabel('ROC AUC')