import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Card, { CardColor, CardValue } from "../components/card";
import Robot, { RobotType } from "../components/robot";
export default function Rules(): JSX.Element {
  return (
    <div className="flex flex-col h-full">
      <LeftPageHeader title="Rules" />

      <div className="flex flex-1">
        <div className="w-1/4 mt-48 p-12">
          <Robot type={RobotType.RULER} />
        </div>

        <div className="w-1/2 px-12 pt-56">
          <div className="flex h-full w-full flex-col justify-center items-center space-y-6 pb-11">
            <TextItem title="Game description">
              The goal of the game is to play all the cards a player is given at
              the beginning as fast as possible. In order to play a card the
              players must follow the &quot;Place a Card&quot;-rules.
              Furthermore, it is possible that players have to pick up new cards
              during the game. This happens if the player of the previous round
              plays certain action cards or if the player does not have any
              playable cards in their hand. <br />
              <br />
              There are different kinds of cards: Normal cards and action cards.
              Normal cards are colored in one of the four colors cyan, orange,
              green and purple and depict one of the numbers from 0-9. Action
              cards can also have one of the colors (&apos;Switch&apos;-,
              &apos;Draw 2&apos;-, and &apos;Skip&apos;-Card) or no color at all
              (&apos;Choose Color&apos;- and &apos;Choose and Draw&apos;-Card).
              The cards without a color can be played any time. The actions that
              are to-be-performed for the action cards are described in more
              detail below.
              <br />
              <br />A player has won the game if they have played all their
              cards. The finishing order of all the players determines the
              placement in the game. (e.g. The player that finishes first gets
              first place. Player that finishes second gets seconds place and so
              on)
            </TextItem>
            <TextItem
              title="Playing a card"
              customImgElement={
                <Card
                  value={CardValue.FOUR}
                  color={CardColor.PURPLE}
                  className="h-48 w-fit"
                />
              }
              center
            >
              A player can play up to one card during their turn. The placed
              card has to fulfil at least one of the following properties in
              order to be playable:
              <ul className="list-disc ml-10">
                <li>
                  The card has the same color as the card on top of the playing
                  pile.
                </li>
                <li>The card has the same face value as the top card</li>
                <li>
                  The card is a &apos;Choose&apos; card or &apos;Choose and
                  Draw&apos; card
                </li>
                <li>
                  If the top card is a &apos;Choose&apos; card or a &apos;Choose
                  and Draw&apos; card and the to-be-played card has the color
                  that was requested while the &apos;Choose&apos; / &apos;Choose
                  Draw&apos; card was played.
                </li>
              </ul>
              If a player has no valid card to play they need to draw one card.
            </TextItem>
            <TextItem title="Playing an invalid card">
              If a player tries to play an invalid card, then this card will not
              be played. Instead, the player has to draw 2 cards as a punishment
              for trying to cheat. If the player performs too many illegal
              actions the player will be disqualified from the game and will not
              receive a score for this game.
            </TextItem>
            <TextItem
              title="'Switch' Card"
              customImgElement={
                <Card
                  value={CardValue.SWITCH}
                  color={CardColor.GREEN}
                  className="h-48 w-fit"
                />
              }
              center
            >
              If a &apos;Switch&apos; card is played then the direction of the
              game is changed. This means that e.g. if the game direction was
              clockwise the game direction will be changed to counter-clockwise.
              That means the previous player is next in turn. The game direction
              will stay that way until another reverse card is played and the
              direction is changed back to clockwise.
            </TextItem>
            <TextItem
              title="'Draw 2' Card"
              customImgElement={
                <Card
                  value={CardValue.DRAW_TWO}
                  color={CardColor.ORANGE}
                  className="h-48 w-fit"
                />
              }
              center
            >
              If a player plays a &apos;Draw 2&apos; card the next player needs
              to draw two cards from the draw pile. However, the next player can
              avoid drawing two cards if they can play a &apos;Draw 2&apos; card
              themselves. The next player thereafter has to draw 4 cards if they
              cannot play a &apos;Draw 2&apos; card. This can go on until no
              player can play another &apos;Draw 2&apos; card. This player then
              needs to draw the accumulated value of cards from the draw pile.
            </TextItem>
            <TextItem
              title="'Skip' Card"
              customImgElement={
                <Card
                  value={CardValue.SKIP}
                  color={CardColor.CYAN}
                  className="h-48 w-fit"
                />
              }
              center
            >
              If a player plays the &apos;Skip&apos; card then the next players
              turn will be skipped.
            </TextItem>
            <TextItem
              title="'Choose Color' Card"
              customImgElement={
                <Card
                  value={CardValue.CHOOSE_COLOR}
                  color={CardColor.GRAY}
                  className="h-48 w-fit"
                />
              }
              center
            >
              The &apos;Choose Color&apos; card allows the player to choose one
              of the four colors. The next player then needs to play a card of
              this choosen color. It is also possible to play another
              &apos;Choose Color&apos; card or a &apos;Choose and Draw&apos;
              card instead.
            </TextItem>
            <TextItem
              title="'Choose and Draw' Card"
              customImgElement={
                <Card
                  value={CardValue.CHOOSE_COLOR_4}
                  color={CardColor.GRAY}
                  className="h-48 w-fit"
                />
              }
              center
            >
              This card works the same as the &apos;Choose Color&apos; card but
              it has the additional effect that the next player needs to
              &apos;Draw 4&apos; cards. The player that needs to draw the 4
              cards can also play another &apos;Choose and Draw&apos; card to
              avoid drawing them. Similar as with the &apos;Draw 2&apos; cards
              the next player then has to draw 8 cards. This can be repeated
              until a player cannot play a &apos;Choose and Draw&apos; card.
              This player then needs to draw the accumulated amount of cards
              from the draw pile. It is not allowed to play a &apos;Draw 2&apos;
              card instead of a &apos;Draw 4&apos; card to avoid drawing. <br />
              E.g. If the player plays a &apos;Draw 2&apos; card on top of a
              &apos;Choose and Draw&apos; card then the player needs to draw the
              accumulated number of cards due to the &apos;Choose and Draw&apos;
              cards from the discard pile. The next player then needs to draw 2
              cards due to the &apos;Draw 2&apos; card.
            </TextItem>

            <TextItem title="Limitations of turn time and resources">
              The players have a fixed amount of time for their turn as well as
              a maximum amount of resources that can be used for calculations.
              If this time / resource limit is exceeded then the player needs to
              draw a punishment card instead of playing a card. If this happens
              too often the player may be disqualified from the game.
            </TextItem>
          </div>
        </div>
      </div>
    </div>
  );
}
