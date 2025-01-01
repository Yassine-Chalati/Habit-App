import 'package:flutter/material.dart';
import 'package:habitmobile/util/const.dart';
import 'package:habitmobile/util/bottom_sheet/sheet_title_row.dart';
import 'package:habitmobile/util/bottom_sheet/input_habit_row.dart';
import 'package:habitmobile/util/bottom_sheet/frequency_row.dart';
import 'package:habitmobile/util/bottom_sheet/color_row.dart';
import 'package:habitmobile/util/bottom_sheet/reminder_row.dart';

 
class MyBottomSheet extends StatelessWidget {
  const MyBottomSheet({super.key});

  @override
  Widget build(BuildContext context) {
    return DraggableScrollableSheet(
        initialChildSize: 0.95,
        builder: (context, scrollController) {
          return Container(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.background,
                  borderRadius: borderRadius),
              child: const BottomSheetColumn());
        });
  }
}

class BottomSheetColumn extends StatelessWidget {
  const BottomSheetColumn({super.key});

  @override
  Widget build(BuildContext context) {
    return Wrap(
      runSpacing: 20,
      children: [
        const SheetTitleRow(),
        InputHabitRow(),
        const ColorRow(),
        Divider(thickness: 2, color: Theme.of(context).dividerColor),
        const FrequencyRow(),
        Divider(thickness: 2, color: Theme.of(context).dividerColor),
        const ReminderRow(),
        Divider(thickness: 2, color: Theme.of(context).dividerColor),
      ],
    );
  }
}
