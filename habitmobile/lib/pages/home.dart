import 'package:flutter/material.dart';
import 'package:habitmobile/data/user_data.dart';
import 'package:provider/provider.dart';
import 'package:habitmobile/data/habit_list.dart';
import 'package:habitmobile/util/habit_tile/habit_tile.dart';
import 'package:habitmobile/pages/bottom_sheet.dart';
import 'package:habitmobile/pages/user_datapage.dart';
import 'package:habitmobile/data/quote.dart';
import 'package:habitmobile/widgets/quotewidget.dart';

class HomePage extends StatelessWidget {
  final Quote dailyQuote = Quote(
    text: "The secret of getting ahead is getting started",
    author: "Mark Twain",
  );

  HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Theme.of(context).colorScheme.background,
      appBar: AppBar(
        title: const Text(
          'Habits',
          style: TextStyle(fontWeight: FontWeight.w600),
        ),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.person), // Icon for user data.
            onPressed: () {
              // Navigate to the user data page.
              Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) =>
                        const UserDatapage()), // User data page.
              );
            },
          ),
        ],
      ),
      body: Column(
        children: [
          // Displaying the daily quote.
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Text(
              '"${dailyQuote.text}"\n- ${dailyQuote.author}',
              textAlign: TextAlign.center,
              style: const TextStyle(
                fontSize: 16,
                fontStyle: FontStyle.italic,
              ),
            ),
          ),
          // Habit view displayed below the quote.
          const Expanded(child: HabitView()),
        ],
      ),
    );
  }
}

class HabitView extends StatelessWidget {
  const HabitView({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<HabitList>(builder: (context, habitList, child) {
      return ListView.builder(
        itemCount: habitList.getHabits.length + 1,
        itemBuilder: (BuildContext context, int index) {
          if (index == habitList.getHabits.length) {
            return const NewHabitRow();
          } else {
            return HabitTile(habitList.getHabits[index]);
          }
        },
      );
    });
  }
}

class NewHabitRow extends StatelessWidget {
  const NewHabitRow({super.key});

  @override
  Widget build(BuildContext context) {
    return IconButton(
      onPressed: () => showModalBottomSheet(
        isScrollControlled: true,
        backgroundColor: Colors.transparent,
        context: context,
        builder: (context) => const MyBottomSheet(),
      ),
      icon: const Icon(Icons.add_circle_outline),
    );
  }
}
