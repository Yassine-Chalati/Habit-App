class UserData {
  final String firstName;
  final String lastName;
  final String email;
  final String gender; // "M" ou "F"
  final int streak; // Nombre de jours d'activité consécutifs

  UserData({
    required this.firstName,
    required this.lastName,
    required this.email,
    required this.gender,
    required this.streak,
  });
}
