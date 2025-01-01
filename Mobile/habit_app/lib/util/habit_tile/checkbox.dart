import 'package:flutter/material.dart';
import 'package:habitmobile/data/habit_conversion.dart';
import 'package:provider/provider.dart';
import 'package:habitmobile/data/habit.dart';
import 'package:habitmobile/data/habit_list.dart';

class CustomCheckbox extends StatefulWidget {
  final Habit habit;
  final int index;
  const CustomCheckbox(this.habit, this.index, {super.key});

  @override
  State<CustomCheckbox> createState() => _CustomCheckboxState();
}

class _CustomCheckboxState extends State<CustomCheckbox> {
  final week = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  bool checked = false;

  bool get active {
    DateTime now = DateTime.now();
    int weekDay = now.weekday;
    DateTime day = now
        .subtract(Duration(days: weekDay - 1))
        .add(Duration(days: widget.index));
    return day.compareTo(now) <= 0 ? true : false;
  }

  int get dayOfWeek {
    DateTime now = DateTime.now();
    int weekDay = now.weekday;
    int dayOfWeek = now
        .subtract(Duration(days: weekDay - 1))
        .add(Duration(days: widget.index))
        .day;
    return dayOfWeek;
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Opacity(
            opacity: 0.5,
            child: Text(
              week[widget.index],
            )),
        SizedBox(
          width: 30,
          height: 30,
          child: Consumer<HabitList>(
              builder: (context, habits, child) => ElevatedButton(
                    onPressed: active
                        ? () {
                            setState(() {
                              widget.habit.doneThisWeek[widget.index] =
                                  !widget.habit.doneThisWeek[widget.index];
                              widget.habit.doneThisWeek[widget.index]
                                  ? widget.habit.done++
                                  : widget.habit.done--;
                              widget.habit.updateDoneThisYear();
                              habits.saveData();
                            });
                          }
                        : null,
                    style: ElevatedButton.styleFrom(
                      elevation: 0,
                      padding: EdgeInsets.zero,
                      shape: const CircleBorder(),
                      backgroundColor: widget.habit.doneThisWeek[widget.index]
                          ? stringToColor(widget.habit.color)
                          : Theme.of(context).splashColor,
                    ),
                    child: Text(dayOfWeek.toString(),
                        style: TextStyle(
                            color: Theme.of(context).colorScheme.onBackground)),
                  )),
        )
      ],
    );
  }
}
