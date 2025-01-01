import 'package:flutter/material.dart';
import 'package:habitmobile/data/habit.dart';
import 'package:habitmobile/pages/habit_info.dart';
import 'package:habitmobile/util/const.dart';
import 'package:habitmobile/util/habit_tile/checkbox.dart';

class HabitTile extends StatelessWidget {
  final Habit habit;
  const HabitTile(this.habit, {super.key});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.of(context).push(
          MaterialPageRoute(
            builder: (context) => HabitInfo(habit: habit),
          ),
        );
      },
      child: Container(
        height: 110,
        decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.surfaceVariant,
            borderRadius: borderRadius),
        margin: blockMargin,
        padding: blockPadding,
        child: Column(
          children: [
            HabitTileText(habit.name, habit.frequency),
            const SizedBox(height: 10),
            HabitTileWeek(habit)
          ],
        ),
      ),
    );
  }
}

class HabitTileText extends StatelessWidget {
  final String name;
  final int frequency;
  const HabitTileText(this.name, this.frequency, {super.key});

  String frequencyToString(int n) {
    return n == 7 ? 'Everyday' : '$n times a week';
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Text(name),
        Opacity(
            opacity: 0.5,
            child: Text(
              frequencyToString(frequency),
            ))
      ],
    );
  }
}

class HabitTileWeek extends StatelessWidget {
  final Habit habit;
  const HabitTileWeek(this.habit, {super.key});

  @override
  Widget build(BuildContext context) {
    return Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
      for (int index = 0; index < 7; index++) CustomCheckbox(habit, index)
    ]);
  }
}
