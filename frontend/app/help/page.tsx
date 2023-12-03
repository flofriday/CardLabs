import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Robot, { RobotType } from "../components/robot";
export default function Help(): JSX.Element {
  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Help" />

      <div className="flex flex-1">
        <div className="w-1/4 mt-48 p-12">
          <Robot type={RobotType.QUESTIONMARK} />
        </div>

        <div className="w-1/2 px-12 pt-56">
          <div className="flex h-full w-full flex-col justify-center items-center space-y-6 pb-11">
            <TextItem title="Programming language">
              In order to code your bot you have to use the programming language
              XY. Here are some websites to get additional help for the
              language:
            </TextItem>
            <TextItem title="Playing a card">
              Congratulations on your decision to embrace the delightful world
              of feline fabulousness! Becoming a cat is a journey of
              sophistication, curiosity, and unabashed lounging. Here are some
              purr-actical tips to master the art of being a cat: Find the
              sunniest spot in the house and claim it as your own. Nap
              frequently; the more unpredictable, the better. Dont forget to
              stretch luxuriously before, during, and after each nap. Chase
              anything that moves, especially if its your tail. Bat at random
              objects as if youre a secret ninja on a mission. A crumpled-up
              paper ball is a world-class toy trust us.
            </TextItem>
            <TextItem title="Drawing a card">
              Dedicate time each day to meticulous self-grooming. Allow your
              human to brush your fur, but only if they insist. Exhibit disdain
              for water-related activities. Master the art of silent judgmental
              stares. Practice the subtle art of headbutts to express affection.
              Perfect the mysterious meow a versatile language of its own.
            </TextItem>
            <TextItem title="Unavailable features">
              There is no IO commands available. The only exception for this is
              the command XY that can be used for debugging.
            </TextItem>
            <TextItem title="Debugging">
              Mark your kingdom with strategic scratching. Gracefully drape
              yourself over essential human belongings. The higher the perch,
              the greater your royal status. Demand the finest culinary
              offerings. Act like each meal is a gourmet experience. Give your
              human the gift of bringing them freshly caught prey (preferably a
              toy mouse).
            </TextItem>
            <TextItem title="Testing your bot">
              Refuse to conform to schedules, except when it comes to mealtime.
              Be selective with your affection let your humans earn it. Exhibit
              a mysterious aura; maintain an air of unapproachable elegance.
            </TextItem>
            <TextItem title="Creating a new bot">
              Remember, being a cat is an art, not a science. Embrace your inner
              feline with grace, curiosity, and an unwavering commitment to the
              pursuit of comfort.
            </TextItem>
            <TextItem title="Editing a bot">Happy catting!</TextItem>
          </div>
        </div>
      </div>
    </div>
  );
}
