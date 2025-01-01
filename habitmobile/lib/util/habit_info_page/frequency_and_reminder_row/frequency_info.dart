import 'package:flutter/material.dart';
import 'package:habitmobile/util/const.dart';

class FrequencyInfo extends StatelessWidget {
  final int frequency;
  const FrequencyInfo(this.frequency, {super.key});

  String frequencyToString(int n) {
    return n == 7 ? 'Everyday' : '$n times a week';
  }

  @override
  Widget build(BuildContext context) {
    var boxDecoration = BoxDecoration(
        color: Theme.of(context).colorScheme.surfaceVariant,
        borderRadius: borderRadius);
    return Container(
      decoration: boxDecoration,
      padding: blockPadding,
      child: Row(
        children: [
          const Icon(
            size: 26,
            Icons.repeat,
            color: Colors.white,
          ),
          const SizedBox(width: 5),
          Opacity(
              opacity: 0.5,
              child: Text(frequencyToString(frequency),
                  style: const TextStyle(fontSize: 15)))
        ],
      ),
    );
  }
}
