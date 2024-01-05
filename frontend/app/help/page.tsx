"use client";

import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Robot, { RobotType } from "../components/robot";
import Markdown from "react-markdown";
import React, { useState, useEffect } from "react";

export default function Help(): JSX.Element {
  const [markdownContent, setMarkdownContent] = useState("");

  useEffect(() => {
    fetch("./cardScheme.md")
      .then(async (response) => await response.text())
      .then((content) => {
        setMarkdownContent(content);
      })
      .catch((error) => {
        console.error("Error fetching Markdown file:", error);
      });
  });

  return (
    <div>
      <LeftPageHeader title="Help" />

      <Robot type={RobotType.QUESTIONMARK} />
      <div className="flex flex-col justify-center items-center pt-52 space-y-6 pb-11">
        <TextItem title="Playing a card">
          Congratulations on your decision to embrace the delightful world of
          feline fabulousness! Becoming a cat is a journey of sophistication,
          curiosity, and unabashed lounging. Here are some purr-actical tips to
          master the art of being a cat: Find the sunniest spot in the house and
          claim it as your own. Nap frequently; the more unpredictable, the
          better. Dont forget to stretch luxuriously before, during, and after
          each nap. Chase anything that moves, especially if its your tail. Bat
          at random objects as if youre a secret ninja on a mission. A
          crumpled-up paper ball is a world-class toy trust us.
        </TextItem>
        <TextItem title="Playing a card">
          Congratulations on your decision to embrace the delightful world of
          feline fabulousness! Becoming a cat is a journey of sophistication,
          curiosity, and unabashed lounging. Here are some purr-actical tips to
          master the art of being a cat: Find the sunniest spot in the house and
          claim it as your own. Nap frequently; the more unpredictable, the
          better. Dont forget to stretch luxuriously before, during, and after
          each nap. Chase anything that moves, especially if its your tail. Bat
          at random objects as if youre a secret ninja on a mission. A
          crumpled-up paper ball is a world-class toy trust us.
        </TextItem>
        <TextItem title="Drawing a card">
          Dedicate time each day to meticulous self-grooming. Allow your human
          to brush your fur, but only if they insist. Exhibit disdain for
          water-related activities. Master the art of silent judgmental stares.
          Practice the subtle art of headbutts to express affection. Perfect the
          mysterious meow a versatile language of its own.
        </TextItem>
        <TextItem title="Unavailable features">
          There is no IO commands available. The only exception for this is the
          command XY that can be used for debugging.
        </TextItem>
        <TextItem title="Debugging">
          Mark your kingdom with strategic scratching. Gracefully drape yourself
          over essential human belongings. The higher the perch, the greater
          your royal status. Demand the finest culinary offerings. Act like each
          meal is a gourmet experience. Give your human the gift of bringing
          them freshly caught prey (preferably a toy mouse).
        </TextItem>
        <TextItem title="Testing your bot">
          Refuse to conform to schedules, except when it comes to mealtime. Be
          selective with your affection let your humans earn it. Exhibit a
          mysterious aura; maintain an air of unapproachable elegance.
        </TextItem>
        <TextItem title="Creating a new bot">
          Remember, being a cat is an art, not a science. Embrace your inner
          feline with grace, curiosity, and an unwavering commitment to the
          pursuit of comfort.
        </TextItem>
        <TextItem title="Editing a bot">Happy catting!</TextItem>
        <TextItem title="Programming Language">
          <Markdown className="prose prose-invert max-w-none">
            {markdownContent}
          </Markdown>
        </TextItem>
      </div>
    </div>
  );
}
