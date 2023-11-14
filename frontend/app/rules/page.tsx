import LeftPageHeader from "../components/leftPageHeader";
import TextItem from "../components/textItem";
import Card, { CardColor, CardValue } from "../components/card";
export default function Rules() {
  return (
    <div>
      <LeftPageHeader title="Rules" />
      <div className="flex h-full flex-col justify-center items-center pt-52 space-y-6">
        <TextItem
          title="Place a Card"
          customImgElement={
            <Card
              value={CardValue.FOUR}
              color={CardColor.PURPLE}
              className="h-48 w-fit"
            />
          }
          center
        >
          A card can be placed if it's the players turn and any of on the
          following rules match:
          <ul className="list-disc ml-10">
            <li>the card has the same color as the top card</li>
            <li>the card has the same face value as the top card</li>
            <li>the card is a "Choose" card or "Choose and Draw" card</li>
            <li>
              if the top card is a "Choose" card or "Choose and Draw" card and
              the "to be placed" card has the same color as the requested color
              form the "Choose" / "Choose Draw" card
            </li>
          </ul>
        </TextItem>
        <TextItem
          title="Switch Card"
          customImgElement={
            <Card
              value={CardValue.SWITCH}
              color={CardColor.GREEN}
              className="h-48 w-fit"
            />
          }
          center
        >
          If a reverse card is played then the direction of the game switchs. If
          it was clockwise beforehand it is counter-clockwise afterwards. That
          means the previous player is next in turn. The direction stays this
          way until another reverse card is played. If this next card is played
          the direction switches back to clockwise.
        </TextItem>
        <TextItem
          title="Draw 2 Card"
          customImgElement={
            <Card
              value={CardValue.DRAW_TWO}
              color={CardColor.ORANGE}
              className="h-48 w-fit"
            />
          }
          center
        >
          If a player plays a "Draw 2" card then the next player needs to draw
          two cards from the draw pile if they don't play a "Draw 2" ontop of
          the already played card. If the play a "Draw 2" then the next player
          needs to draw 4 cards. This goes forth until one player can't or wont
          play another "Draw 2" card. This player then needs to draw the
          accumulated value of cards form the draw pile.
        </TextItem>
        <TextItem
          title="Skip Card"
          customImgElement={
            <Card
              value={CardValue.SKIP}
              color={CardColor.CYAN}
              className="h-48 w-fit"
            />
          }
          center
        >
          If a player plays the skip card then the next players turn will be
          skipped.
        </TextItem>
        <TextItem
          title="Choose Color Card"
          customImgElement={
            <Card
              value={CardValue.CHOOSE_COLOR}
              color={CardColor.GRAY}
              className="h-48 w-fit"
            />
          }
          center
        >
          This card allows the player to choose one of the four colors. The next
          player then needs to play a card of this choosen color. It is also
          possible to play another "Choose Color" card or a "Choose and Draw"
          card
        </TextItem>
        <TextItem
          title="Choose and Draw Card"
          customImgElement={
            <Card
              value={CardValue.CHOOSE_COLOR_4}
              color={CardColor.GRAY}
              className="h-48 w-fit"
            />
          }
          center
        >
          This card works the same as the "Choose Color" card, but also has the
          effect that the next player needs to draw 4 cards. The player the
          needs to draw the 4 cards can also play another "Choose and Draw"
          card. If this happens then the next player needs to draw 8 cards. This
          goes forth until a player no longer wants or can play anoter "Choose
          and Draw" card. This player then needs to draw the accumulated amount
          of cards from the draw pile. It is not allowed to play a "Draw 2" card
          ontop of the "Chose and Draw" card with the intend pass along the draw
          amount.
          <br />
          e.g. If the player plays a "Draw 2" card ontop of a "Choose and Draw"
          card then the player needs to draw the accumulated number of cards due
          to the "Choose and Draw" cards from the discard pile. The next player
          then needs to draw 2 cards due to the "Draw 2" card.
        </TextItem>
        <TextItem title="Win the Game">
          The game is won if the player has no more cards on their hand. The
          finishing order of all the players will define the placement in the
          game.
          <br />
          e.g. Player that finishes first gets first place. Player that finishes
          second gets seconds place and so forth.
        </TextItem>
        <TextItem title="Wrong Card Played">
          If a player tries to play an invalid card, then the player needs to
          draw a punishment card. If the player performs too many illegal
          actions the player may be removed from the game.
        </TextItem>
        <TextItem title="Max Turn Time">
          The players have a fixed amount of time for their turn if this time is
          exceeded then the player needs to draw a punishment card. If this
          happens too often the player may be removed from the game.
        </TextItem>
        <TextItem title="Max Ressources">
          If the player needs too many ressources in their turn they may be
          removed from the game.
          <br />
          e.g The player allocates 1GB of memory.
        </TextItem>
      </div>
    </div>
  );
}
