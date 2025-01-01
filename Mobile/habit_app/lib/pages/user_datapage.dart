import 'package:flutter/material.dart';
import 'package:get/get.dart'; // Assurez-vous d'importer Get pour la navigation
import 'package:habitmobile/pages/UpdateProfile.dart'; // Assurez-vous d'importer la page UpdateProfileScreen

class UserDatapage extends StatefulWidget {
  const UserDatapage({super.key});

  @override
  _ProfilePageState createState() => _ProfilePageState();
}

class _ProfilePageState extends State<UserDatapage> {
  // Données simulées de l'utilisateur
  String firstName = "John";
  String lastName = "Doe";
  String email = "john.doe@example.com";
  String gender = "M"; // M ou F

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Profile'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: const EdgeInsets.all(20),
          child: Column(
            children: [
              // Profile image display only (not editable)
              const SizedBox(height: 20),
              CircleAvatar(
                radius: 50,
                backgroundImage: AssetImage('assets/images/profile.png'),
              ),
              const SizedBox(height: 20),

              // -- AFFICHAGE DES DONNÉES
              Text(
                "$firstName $lastName",
                style: Theme.of(context)
                    .textTheme
                    .titleLarge
                    ?.copyWith(fontWeight: FontWeight.bold),
              ),
              Text(
                email,
                style: Theme.of(context).textTheme.bodyMedium,
              ),
              const SizedBox(height: 20),

              // -- GENDER
              Text(
                'Gender: $gender',
                style: Theme.of(context).textTheme.bodyMedium,
              ),
              const SizedBox(height: 20),

              // -- BOUTON POUR MODIFIER LE PROFIL
              SizedBox(
                width: 200,
                child: ElevatedButton(
                  onPressed: () {
                    Get.to(() =>
                        UpdateProfileScreen()); // Navigation vers la page de mise à jour du profil
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blue, // Couleur principale
                    side: BorderSide.none,
                    shape: const StadiumBorder(),
                  ),
                  child: const Text("Edit Profile",
                      style: TextStyle(color: Colors.white)),
                ),
              ),
              const SizedBox(height: 30),

              // -- MENU
              const Divider(),
              const SizedBox(height: 10),

              // Menu pour les autres actions (Paramètres, Détails de facturation, etc.)
              ProfileMenuWidget(
                title: "Logout",
                icon: Icons.exit_to_app,
                textColor: Colors.red,
                endIcon: false,
                onPress: () {
                  // Logic to handle logout
                  showDialog(
                    context: context,
                    builder: (_) => AlertDialog(
                      title: const Text("LOGOUT"),
                      content: const Padding(
                        padding: EdgeInsets.symmetric(vertical: 15.0),
                        child: Text("Are you sure you want to logout?"),
                      ),
                      actions: [
                        ElevatedButton(
                          onPressed: () {
                            // Ajouter la logique de déconnexion ici
                          },
                          child: const Text("Yes"),
                        ),
                        OutlinedButton(
                          onPressed: () => Navigator.of(context).pop(),
                          child: const Text("No"),
                        ),
                      ],
                    ),
                  );
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class ProfileMenuWidget extends StatelessWidget {
  final String title;
  final IconData icon;
  final VoidCallback onPress;
  final Color textColor;
  final bool endIcon;

  const ProfileMenuWidget({
    required this.title,
    required this.icon,
    required this.onPress,
    this.textColor = Colors.black,
    this.endIcon = true,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      onTap: onPress,
      trailing: endIcon ? const Icon(Icons.arrow_forward_ios) : null,
    );
  }
}
