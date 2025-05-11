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


%cereales
cereales(maiz, 86).  
cereales(arroz_blanco, 130).  
cereales(arroz_integral, 111).  


%Varios tipos de preparaciones para el huevo             
huevo(huevo_duro, 155).               
huevo(huevo_frito, 196).              
huevo(huevo_revuelto_sin_aceite, 154). 
huevo(huevos_a_la_mexicana, 120).      
huevo(huevos_rancheros, 150).         
huevo(huevos_divorciados, 160).       
huevo(huevos_con_chorizo, 210).       
huevo(huevos_motuleños, 220).         
huevo(omelette_basico, 154).          


%proteinas
% pollo
proteina(pollo_pechuga_plancha, 165).  
proteina(pollo_pechuga_horno, 170).    
proteina(pollo_muslo_horno, 215).  
proteina(pollo_entero_asado, 190).  
% res
proteina(res_filete_crudo, 250).  
proteina(res_filete_plancha, 271). 
% cerdo 
proteina(cerdo_lomo_crudo, 143).  
proteina(cerdo_lomo_plancha, 200).  
proteina(cerdo_costillas, 320).  
% pescado
proteina(salmon_a_la_plancha, 208).    
proteina(atun_en_agua, 128).  
% alternativas veganas
proteina(tofu, 76).  
proteina(tempeh, 193).  
proteina(lentejas_cocidas, 116).  
proteina(garbanzos_cocidos, 164).  
proteina(quinua_cocida, 120).



%Pastas
pasta(pasta_huevo, 368).
pasta(pasta_semola, 361).
pasta(plato_spaghetti_bolognesa, 500).      
pasta(plato_fettuccine_alfredo, 450).         
pasta(plato_pasta_pesto, 600).              
pasta(plato_lasaña_carne, 687).             
pasta(plato_ravioles_queso, 700).           
pasta(porcion_spaghetti_solo, 800).         
pasta(porcion_gnocchi_solo, 812).           
pasta(plato_tallarines_rojos, 718).               


%Postre
postre(flan_vainilla, 102).
postre(flan_huevo, 126).
postre(gelatina_sin_azucar, 25).       
postre(gelatina_con_azucar, 87).      
postre(fresas_con_crema_light, 150).  
postre(yogur_natural_descremado, 75).  
postre(compota_de_manzana, 100).       
postre(mousse_de_limon_light, 112).    
postre(helado_de_agua, 125).           
    
%Lacteo
lacteo(leche_desnatada, 34).        
lacteo(leche_deslactosada, 49).          
lacteo(leche_en_polvo, 496).        
lacteo(yogur_natural_entero, 59).    
lacteo(yogur_griego, 97).           
lacteo(yogur_de_frutas, 90).                  
lacteo(queso_cheddar, 402).         
lacteo(queso_gouda, 356).                  
lacteo(leche_soja, 54).             
lacteo(leche_almendras, 15).        
lacteo(leche_avena, 43).            
lacteo(yogur_coco, 180).            

%Colacion
colacion(donut, 456).
colacion(pastel_manzana, 311).
colacion(almendras_30g, 170).
colacion(yogur_natural_sin_azucar, 100).
colacion(palitos_de_apio_con_crema_de_cacahuate, 120).
colacion(barrita_de_avena_casera, 150).
colacion(galletas_integrales_2_unidades, 90).
colacion(rollitos_de_jamon_de_pavo_con_aguacate, 130).
colacion(nueces_20g, 140).
colacion(zanahorias_con_hummus, 110).
colacion(kiwi, 50).
colacion(requeson_con_fresas, 120).



% Regla para armar el desayuno
desayuno(D1, D2, D3, D4, KCal) :-
    fruta(D1, K1), cereales(D2, K2), huevo(D3, K3), lacteo(D4,K4),
    KCal is K1 + K2 + K3 + K4.

%Regla para tener la primer colacion
colacion1(P1,P2, KCal):-
    colacion(P1,K1), fruta(P2,K2),
    KCal is K1 + K2.

% Regla para armar la comida
comida(C1, C2, C3, KCal) :-
    proteina(C1, K1), pasta(C2, K2), postre(C3, K3),
    KCal is K1 + K2 + K3.

%Regla para obtener la segunda colacion
colacion2(Q1,Q2, KCal):-                
    colacion(Q1,K1), fruta(Q2,K2),
    KCal is K1 + K2.


% Regla para armar la merienda
merienda(M1, M2, KCal) :-
    lacteo(M1, K1), colacion(M2, K2),
    KCal is K1 + K2.


% Regla que genera lista de menus

dietas(Gasto, ListaMenus) :-
    findnsols(100, menu([D1,D2,D3,D4],[P1,P2],[C1,C2,C3],[Q1,Q2],[M1,M2],TotalKCal),(
        desayuno(D1,D2,D3,D4,K1),
        colacion1(P1,P2,K4),
        comida(C1,C2,C3,K2),
        colacion2(Q1,Q2,K5),
        merienda(M1,M2,K3),
        TotalKCal is K1 + K2 + K3 + K4 + K5,
        Inferior is Gasto * 0.9,
        Superior is Gasto * 1.1,
        TotalKCal >= Inferior,
        TotalKCal =< Superior
    ), ListaMenus).