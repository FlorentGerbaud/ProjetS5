1. A PROPOS DE TraitementImageNoirBlanc.java

J'ai entièrement refait la structure comme je t'avais dit les matrices étaient inutiles,
je les ai donc supprimer. Maintenant, on stocke le résultat de chaque traitement l'attribut 
post_process_pixels qui est un tableau de byte. C'est donc plus propre. Je te laisse y jeter un oeil.

2. A PROPOS DE TraitementImageCouleur.java

/**
 * FLORENT LIT BIEN CE PASSAGE POUR COMPRENDRE COMMENT ON VA STRUCTURER CETTE PARTIE : 
 * On fait exactement comme avant avec le tableau de pixels qui contient les pixels de l'image.
 * Le constructeur se charge de le remplir directement.
 * Par contre, comme tu peux le voir, j'ai mit le tableau de pixels en 2D en attribut de la classe.
 * En gros, au lieu de renvoyer après chaque traitement, une matrice, on modifie directement l'attribut matrice.
 * Mais comme tu l'aura devinés à chaque appel d'une nouvelle méthode de traitement, on perd le résultat de la
 * méthode précédente. 
 * En réalité, ça n'a aucune conséquence puisque que dans l'interface on appel les méthodes les unes après les
 * autres. Donc à chaque appel on va écraser le résultat de la méthode précédente.
 * Voilà c'est tout. 
 * De cette manière ça rend le code plus propre et ça évite des returns dans tous les sens. 
 * On finira par transférer le résultat stocker dans la matrice dans l'attribut post_process_pixels pour écriture 
 dans un fichier .png
 */

3. A PROPOS DE TestRecupImage

TestRecupImage est l'endroit ou on test nos fonctions une fois qu'on sait qu'elles fonctionnent bien,
on l'implémente dans InterfaceForUser.java.

Essaye de toujours préciser comment on lance les méthodes et quelles sont les arguments.
(je l'ai précisé dans TestRecupImage pour la première partie comme ça tu pourras tester via la nouvelles 
transformation i.e suppresion des matrices pour les images NOIRBLANC)





