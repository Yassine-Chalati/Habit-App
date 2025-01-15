import 'package:get/get.dart';
import 'package:habitmobile/pages/login_screen.dart';
import 'package:habitmobile/pages/register_screen.dart';
import 'package:habitmobile/pages/home.dart';
import 'package:habitmobile/pages/onboarding_page.dart'; // Importez la page Onboarding

class Routes {
  // Définir les routes ici
  static const String login = '/login';
  static const String register = '/register';
  static const String home = '/home'; // Page d'accueil
  static const String onboarding = '/onboarding'; // Page Onboarding

  // Définition de toutes les pages associées aux routes
  static List<GetPage> pages = [
    GetPage(
        name: login,
        page: () => const LoginPage()), // Route pour la page de login
    GetPage(
        name: register,
        page: () =>
            const RegisterPage()), // Route pour la page d'enregistrement
    GetPage(name: home, page: () => HomePage()), // Route pour la page d'accueil
    GetPage(
        name: onboarding,
        page: () => const OnboardingPage()), // Route pour la page onboarding
  ];
}
