import 'package:flutter/material.dart';
import 'package:get/get.dart'; // Assurez-vous d'importer Get
import 'package:font_awesome_flutter/font_awesome_flutter.dart'; // Importation du package font_awesome_flutter

// Définissez vos autres imports si nécessaire

class UpdateProfileScreen extends StatelessWidget {
  const UpdateProfileScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    ; // Assurez-vous que ProfileController est défini quelque part
    return Scaffold(
      appBar: AppBar(
        leading: IconButton(
            onPressed: () => Get.back(),
            icon: const Icon(FontAwesomeIcons
                .angleLeft)), // Remplacez LineAwesomeIcons.angle_left par FontAwesomeIcons.angleLeft
        title: Text('Edit Profile',
            style: Theme.of(context).textTheme.titleMedium),
      ),
      body: SingleChildScrollView(
        child: Container(
          padding: const EdgeInsets.all(
              16.0), // Remplacez tDefaultSize par la valeur réelle ou importez-la
          child: Column(
            children: [
              // -- IMAGE with ICON
              Stack(
                children: [
                  SizedBox(
                    width: 120,
                    height: 120,
                    child: ClipRRect(
                      borderRadius: BorderRadius.circular(100),
                      child: const Image(
                          image: AssetImage(
                              'assets/profile.jpeg')), // Remplacez par votre image
                    ),
                  ),
                  Positioned(
                    bottom: 0,
                    right: 0,
                    child: Container(
                      width: 35,
                      height: 35,
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(100),
                        color: Colors
                            .blue, // Remplacez tPrimaryColor par une couleur réelle
                      ),
                      child: const Icon(
                        FontAwesomeIcons
                            .camera, // Remplacez LineAwesomeIcons.camera par FontAwesomeIcons.camera
                        color: Colors.black,
                        size: 20,
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 50),

              // -- Form Fields
              Form(
                child: Column(
                  children: [
                    TextFormField(
                      decoration: const InputDecoration(
                          label: Text('Full Name'),
                          prefixIcon: Icon(FontAwesomeIcons
                              .user)), // Remplacez LineAwesomeIcons.user par FontAwesomeIcons.user
                    ),
                    const SizedBox(
                        height:
                            20), // Remplacez tFormHeight - 20 par la hauteur que vous souhaitez
                    TextFormField(
                      decoration: const InputDecoration(
                          label: Text('Email'),
                          prefixIcon: Icon(FontAwesomeIcons
                              .envelope)), // Remplacez LineAwesomeIcons.envelope_1 par FontAwesomeIcons.envelope
                    ),
                    const SizedBox(height: 20),
                    TextFormField(
                      decoration: const InputDecoration(
                          label: Text('Phone No'),
                          prefixIcon: Icon(FontAwesomeIcons
                              .phone)), // Remplacez LineAwesomeIcons.phone par FontAwesomeIcons.phone
                    ),
                    const SizedBox(height: 20),
                    TextFormField(
                      obscureText: true,
                      decoration: InputDecoration(
                        label: const Text('Password'),
                        prefixIcon: const Icon(Icons.fingerprint),
                        suffixIcon: IconButton(
                            icon: const Icon(FontAwesomeIcons.eyeSlash),
                            onPressed:
                                () {}), // Remplacez LineAwesomeIcons.eye_slash par FontAwesomeIcons.eyeSlash
                      ),
                    ),
                    const SizedBox(height: 20),

                    // -- Form Submit Button
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: () {},
                        style: ElevatedButton.styleFrom(
                            backgroundColor:
                                Colors.blue, // Remplacez tPrimaryColor
                            side: BorderSide.none,
                            shape: const StadiumBorder()),
                        child: const Text('Save',
                            style: TextStyle(
                                color: Colors.white)), // Remplacez tEditProfile
                      ),
                    ),
                    const SizedBox(height: 20),

                    // -- Created Date and Delete Button
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text.rich(
                          TextSpan(
                            text: 'Joined: ',
                            style: TextStyle(fontSize: 12),
                            children: [
                              TextSpan(
                                  text:
                                      '12/01/2023', // Remplacez tJoinedAt par une date réelle
                                  style: TextStyle(
                                      fontWeight: FontWeight.bold,
                                      fontSize: 12))
                            ],
                          ),
                        ),
                        ElevatedButton(
                          onPressed: () {},
                          style: ElevatedButton.styleFrom(
                              backgroundColor:
                                  Colors.redAccent.withOpacity(0.1),
                              elevation: 0,
                              foregroundColor: Colors.red,
                              shape: const StadiumBorder(),
                              side: BorderSide.none),
                          child: const Text('Delete'), // Remplacez tDelete
                        ),
                      ],
                    )
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
