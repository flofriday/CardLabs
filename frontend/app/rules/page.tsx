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
              players must follow the &quot;Card Matching&quot;-rules which are
              further described below.
              <br />
              <br />
              The game starts with each player holding 7 random cards. Another
              random card from the draw pile is moved to the pile as the
              starting card. Now the players in clockwise order take turn until
              one wins or gets disqualified.
              <br />
              <br />A player wins the game by playing all their cards. Only the
              first player wins and all other players are losers and the game
              ends.
            </TextItem>
            <TextItem title="Playing a card" center>
              All cards are colored in one of the following colors orange,
              green, cyan or purple.
              <br />
              <br />
              If a player has any matching cards they must play a card in their
              turn. The placed card has to fulfil at least one of the following
              properties in order to be considered a match:
              <ul className="list-disc ml-10">
                <li>
                  The card has the same color as the top card of the playing
                  pile.
                </li>
                <li>The card has the same face as the top card</li>
                <li>
                  The &apos;Choose&apos; and &apos;Choose and Draw&apos; cards
                  can be played at any time.
                </li>
                <li>
                  The card matches the color that was previously requested by
                  the preceding &apos;Choose&apos; or &apos;Choose and
                  Draw&apos; card.
                </li>
              </ul>
            </TextItem>
            <TextItem title="Picking up cards">
              If the player doesn&apos;t have any matching cards they can play,
              a card will automatically picked up for them. Cards will also
              picked up automatically if the previous player plays a card with a
              draw cards effect.
              <br />
              <br />
              Some similar card games have rules that players have to announce
              the number of cards they hold if this number is small enough.
              Since that isn&apos;t algorithmically interesting, no such rule
              exists for this game.
            </TextItem>
            <TextItem title="Disqualification">
              If a player tries to play an invalid card, they will be
              disqualified and the game ends in an instance. Players are also
              disqualified if they play cards they don&apos;t currently hold or
              if their code crashes. The bot is then blocked from playing any
              future games. For all other players in the game this doesn&apos;t
              have any impact on their scores.
            </TextItem>
            <TextItem
              title="'Number' Card"
              customImgElement={
                <Card
                  value={CardValue.FOUR}
                  color={CardColor.PURPLE}
                  className="h-48 w-fit"
                />
              }
              center
            >
              This is the basic card of the game which consists of a color and a
              number between 0 and 9 (inclusive).
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
              <br />
              <br />
              If only two players are playing the card behaves like a
              &apos;Skip&apos; Card.
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
              to unconditionally draw two cards from the draw pile. This cannot
              be avoided by the next player playing another &apos;Draw 2&apos;
              card.
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
              this choosen color.
              <br />
              <br />
              Even though not advantageous to the player, they can choose not to
              wish a color by selecting the special &apos;Any&apos; color, which
              allows the next player to play any card.
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
              &apos;Draw 4&apos; cards. This cannot be avoided by the next
              player playing another &apos;Choose And Draw&apos; card.
            </TextItem>

            <TextItem title="Limitations of turn time and resources">
              The players have a fixed amount of computation time for each turn
              as well as a maximum amount of resources that can be used for
              calculations. If this time / resource limit is exceeded the player
              get&apos;s disqualified.
            </TextItem>
          </div>
        </div>
      </div>
    </div>
  );
}
