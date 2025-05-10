%Frutas
fruta(manzana, 52).  
fruta(platano, 89).  
fruta(naranja, 47).  
fruta(fresa, 32).  
fruta(kiwi, 61).  
fruta(uva, 69).  
fruta(mango, 60).  
fruta(piña, 50).  
fruta(sandia, 30).  
fruta(melon, 34).  
fruta(pera, 57).  
fruta(ciruela, 46).  
fruta(arandano, 57).  
fruta(frambuesa, 52).  
fruta(mora, 43).  
fruta(papaya, 43).  
fruta(guayaba, 68).  
fruta(higo, 74).  
fruta(granada, 83).  
fruta(cereza, 50).  


%cereales
cereales(maiz, 86).  
cereales(arroz_blanco, 130).  
cereales(arroz_integral, 111).  
cereales(avena, 389).  
cereales(trigo, 340).  
cereales(quinoa, 120).  
cereales(cebada, 354).  
cereales(centeno, 338).  
cereales(mijo, 378).  
cereales(amaranto, 371).  
cereales(maiz_tortilla, 55).  
cereales(palomitas_maiz, 375).  
cereales(harina_maiz, 364).  
cereales(corn_flakes, 357).  
cereales(pan_blanco, 265).  
cereales(pan_integral, 247).  
cereales(harina_trigo, 364).  
cereales(pasta_trigo, 131).  
cereales(galletas_trigo, 408).  
cereales(avena_copos, 389).  
cereales(barra_avena, 412).  


%bebidas
bebidas(zumo_naranja, 42).


%Huevo
huevo(huevo_estrellado, 147).

%Carnico
carnico(chuleta_cerdo, 330).
carnico(chicharron, 601).

%Pastas
pasta(pasta_huevo, 368).
pasta(pasta_semola, 361).

%Postre
postre(flan_vainilla, 102).
postre(flan_huevo, 126).

%Lacteo
lacteo(leche_entera, 68).
lacteo(leche_semidescremada, 49).

%Colacion
colacion(donut, 456).
colacion(pastel_manzana, 311).

% Regla para armar el desayuno
desayuno(D1, D2, D3, D4, KCal) :-
    fruta(D1, K1), cereales(D2, K2), bebidas(D3, K3), huevo(D4, K4),
    KCal is K1 + K2 + K3 + K4.

% Regla para armar la comida
comida(C1, C2, C3, KCal) :-
    carnico(C1, K1), pasta(C2, K2), postre(C3, K3),
    KCal is K1 + K2 + K3.

% Regla para armar la merienda
merienda(M1, M2, KCal) :-
    lacteo(M1, K1), colacion(M2, K2),
    KCal is K1 + K2.

% Regla que genera lista de menus
dietas(Gasto, ListaMenus) :-
    findall(menu([D1,D2,D3,D4],[C1,C2,C3],[M1,M2],TotalKCal), (
        desayuno(D1,D2,D3,D4,K1),
        comida(C1,C2,C3,K2),
        merienda(M1,M2,K3),
        TotalKCal is K1 + K2 + K3,
        Inferior is Gasto * 0.9,
        Superior is Gasto * 1.1,
        TotalKCal >= Inferior,
        TotalKCal =< Superior
    ), ListaMenus).


