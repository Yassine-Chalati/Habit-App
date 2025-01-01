import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:habitmobile/util/notification_service.dart';
import 'package:habitmobile/data/habit.dart';
import 'package:habitmobile/data/habit_list.dart';
import 'package:habitmobile/pages/onboarding_page.dart'; // Importez la page Onboarding
import 'package:habitmobile/pages/home.dart'; // Importez la page Home
import 'package:get/get.dart'; // Importer correctement Get pour utiliser GetMaterialApp
import 'routes.dart'; // Assurez-vous d'importer le fichier routes.dart

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await LocalNoticeService().setup();
  await Hive.initFlutter();
  Hive.registerAdapter(HabitAdapter());
  Box box = await Hive.openBox('box');

  runApp(
    ChangeNotifierProvider(
      create: (context) => HabitList(),
      child: GetMaterialApp(
        debugShowCheckedModeBanner: false,
        initialRoute: Routes.onboarding,
        getPages: Routes.pages, // Enregistrer toutes les pages ici
        theme: ThemeData.dark(), // Utilisez le thème sombre global
        darkTheme:
            ThemeData.dark(), // Spécifiez que le thème sombre est par défaut
        themeMode:
            ThemeMode.dark, // Force l'application à utiliser le thème sombre
      ),
    ),
  );
}
