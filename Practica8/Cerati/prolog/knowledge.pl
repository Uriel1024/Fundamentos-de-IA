%BASE DE CONOCIMIENTOS
%(Estos son solo ejemplos, no es información veridica)

frame(reino_animalia,subclase_de(top),
      propiedades([necesita(respirar)]),
      descripcion('Los animales son organismos multicelulares')).

frame(clase_insecta,subclase_de(reino_animalia),
      propiedades([tiene(exoesqueleto)]),
      descripcion('Los insectos son la clase mas diversa del reino animal')).

frame(clase_reptilia,subclase_de(reino_animalia),
      propiedades([tiene(sangre_fria)]),
      descripcion('Los reptiles son animales vertebrados que tienen como principal caracteristica tener la piel escamosa')).

frame(luciernaga,subclase_de(clase_insecta),
      propiedades([especie(lampyris_noctiluca),tiene(trasero_luminoso)]),
      descripcion('Las luciernagas son insectos que producen luz mediante bioluminiscencia')).

frame(caiman,subclase_de(clase_reptilia),
      propiedades([especie(caiman_crocodilus),tiene(hocico_grande)]),
      descripcion('Los caimanes son reptiles de agua dulce')).

