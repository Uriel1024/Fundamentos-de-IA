% ——————————————
%  HECHOS NUTRICIONALES
% ——————————————

% Frutas
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

% Cereales
cereales(maiz, 86).
cereales(arroz_blanco, 130).
cereales(arroz_integral, 111).

% Jugos (reincorporados para breakfast)
jugo(zumo_naranja, 42).
jugo(zumo_manzana, 50).

% Huevos
huevo(huevo_duro, 155).
huevo(huevo_frito, 196).
huevo(huevo_revuelto_sin_aceite, 154).
huevo(huevos_a_la_mexicana, 120).
huevo(huevos_rancheros, 150).
huevo(huevos_divorciados, 160).
huevo(huevos_con_chorizo, 210).
huevo(huevos_motuleños, 220).
huevo(omelette_basico, 154).

% Proteínas (en lugar de 'carnico')
proteina(pollo_pechuga_plancha, 165).
proteina(pollo_pechuga_horno, 170).
proteina(pollo_muslo_horno, 215).
proteina(pollo_entero_asado, 190).
proteina(res_filete_crudo, 250).
proteina(res_filete_plancha, 271).
proteina(cerdo_lomo_crudo, 143).
proteina(cerdo_lomo_plancha, 200).
proteina(cerdo_costillas, 320).
proteina(salmon_a_la_plancha, 208).
proteina(atun_en_agua, 128).
proteina(tofu, 76).
proteina(tempeh, 193).
proteina(lentejas_cocidas, 116).
proteina(garbanzos_cocidos, 164).
proteina(quinua_cocida, 120).

% Pastas
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

% Postres
postre(flan_vainilla, 102).
postre(flan_huevo, 126).
postre(gelatina_sin_azucar, 25).
postre(gelatina_con_azucar, 87).
postre(fresas_con_crema_light, 150).
postre(yogur_natural_descremado, 75).
postre(compota_de_manzana, 100).
postre(mousse_de_limon_light, 112).
postre(helado_de_agua, 125).

% Lácteos
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

% Colaciones
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




% Desayuno: fruta + cereal + huevo + lácteo
desayuno(D1, D2, D3, D4, K) :-
    fruta(D1, K1),
    cereales(D2, K2),
    huevo(D3, K3),
    lacteo(D4, K4),
    K is K1 + K2 + K3 + K4.

% Primera colación: colación + fruta
colacion1(P1, P2, K) :-
    colacion(P1, K1),
    fruta(P2, K2),
    K is K1 + K2.

% Comida principal: proteína + pasta + postre
comida(C1, C2, C3, K) :-
    proteina(C1, K1),
    pasta(C2, K2),
    postre(C3, K3),
    K is K1 + K2 + K3.

% Segunda colación: colación + fruta
colacion2(Q1, Q2, K) :-
    colacion(Q1, K1),
    fruta(Q2, K2),
    K is K1 + K2.

% Merienda: lácteo + colación
merienda(M1, M2, K) :-
    lacteo(M1, K1),
    colacion(M2, K2),
    K is K1 + K2.

% -------------------------------
%  REGLA PRINCIPAL con findnsols/3
% -------------------------------

dietas(Gasto, ListaMenus) :-
    Inferior is Gasto * 0.9,
    Superior is Gasto * 1.1,

    % Limita a las primeras 100 soluciones
    findnsols(
      100,
      menu(
        [D1,D2,D3,D4],    % desayuno
        [P1,P2],          % colación1
        [C1,C2,C3],       % comida
        [Q1,Q2],          % colación2
        [M1,M2],          % merienda
        TotalKCal
      ),
      (
        desayuno(D1,D2,D3,D4, K1),
        colacion1(P1,P2,     K2),
        comida(C1,C2,C3,     K3),
        colacion2(Q1,Q2,     K4),
        merienda(M1,M2,      K5),
        TotalKCal is K1 + K2 + K3 + K4 + K5,
        TotalKCal >= Inferior,
        TotalKCal =< Superior
      ),
      ListaMenus
    ).
