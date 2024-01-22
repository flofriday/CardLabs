export enum CardColor {
  CYAN = "#01ffff",
  ORANGE = "#fe4600",
  GREEN = "#68f200",
  PURPLE = "#9819cb",
  GRAY = "#3a4142",
  ANY = "#3a4142",
}

export const CardValue = {
  ZERO: {
    text: "0",
    x1Offset: 1,
    y1Offset: 4,
    x2Offset: 0,
    y2Offset: 3,
    x3Offset: 1,
    y3Offset: 4,
    fontSize: 32,
  },
  ONE: {
    text: "1",
    x1Offset: 5,
    y1Offset: 4,
    x2Offset: 3,
    y2Offset: 4,
    x3Offset: 5,
    y3Offset: 4,
    fontSize: 32,
  },
  TWO: {
    text: "2",
    x1Offset: 2,
    y1Offset: 2,
    x2Offset: 1,
    y2Offset: 3,
    x3Offset: 2,
    y3Offset: 3,
    fontSize: 32,
  },
  THREE: {
    text: "3",
    x1Offset: 2,
    y1Offset: 3,
    x2Offset: 1,
    y2Offset: 3,
    x3Offset: 2,
    y3Offset: 3,
    fontSize: 32,
  },
  FOUR: {
    text: "4",
    x1Offset: 0,
    y1Offset: 2,
    x2Offset: 0,
    y2Offset: 2,
    x3Offset: -1,
    y3Offset: 3,
    fontSize: 32,
  },
  FIVE: {
    text: "5",
    x1Offset: 2,
    y1Offset: 3,
    x2Offset: 0,
    y2Offset: 3,
    x3Offset: 2,
    y3Offset: 4,
    fontSize: 32,
  },
  SIX: {
    text: "6",
    x1Offset: 2,
    y1Offset: 3,
    x2Offset: 1,
    y2Offset: 3,
    x3Offset: 2,
    y3Offset: 4,
    fontSize: 32,
  },
  SEVEN: {
    text: "7",
    x1Offset: 4,
    y1Offset: 3,
    x2Offset: 3,
    y2Offset: 4,
    x3Offset: 3,
    y3Offset: 4,
    fontSize: 32,
  },
  EIGHT: {
    text: "8",
    x1Offset: 2,
    y1Offset: 2,
    x2Offset: 0,
    y2Offset: 3,
    x3Offset: 1,
    y3Offset: 3,
    fontSize: 32,
  },
  NINE: {
    text: "9",
    x1Offset: 3,
    y1Offset: 3,
    x2Offset: 1,
    y2Offset: 4,
    x3Offset: 2,
    y3Offset: 4,
    fontSize: 32,
  },
  SWITCH: {
    text: "⇄",
    x1Offset: -4,
    y1Offset: 0,
    x2Offset: -5,
    y2Offset: 0,
    x3Offset: -4,
    y3Offset: 1,
    fontSize: 32,
  },
  SKIP: {
    text: "⦸",
    x1Offset: -5,
    y1Offset: 0,
    x2Offset: -8,
    y2Offset: 2,
    x3Offset: -6,
    y3Offset: 2,
    fontSize: 32,
  },
  DRAW_TWO: {
    text: "+²",
    x1Offset: 0,
    y1Offset: 3,
    x2Offset: -5,
    y2Offset: 3,
    x3Offset: -3,
    y3Offset: 5,
    fontSize: 32,
  },
  CHOOSE_COLOR: {
    text: "⊞",
    x1Offset: -2,
    y1Offset: -2,
    x2Offset: -5,
    y2Offset: 1,
    x3Offset: -2,
    y3Offset: 0,
    fontSize: 26,
  },
  CHOOSE_COLOR_4: {
    text: "⊞⁴",
    x1Offset: -3,
    y1Offset: -2,
    x2Offset: -10,
    y2Offset: 3,
    x3Offset: -5,
    y3Offset: 1,
    fontSize: 26,
  },
} as const;

export function toCardType(type: string, number?: number): CardValue_ {
  if (type === "NUMBER_CARD") {
    if (number === 0) {
      return CardValue.ZERO;
    } else if (number === 1) {
      return CardValue.ONE;
    } else if (number === 2) {
      return CardValue.TWO;
    } else if (number === 3) {
      return CardValue.THREE;
    } else if (number === 4) {
      return CardValue.FOUR;
    } else if (number === 5) {
      return CardValue.FIVE;
    } else if (number === 6) {
      return CardValue.SIX;
    } else if (number === 7) {
      return CardValue.SEVEN;
    } else if (number === 8) {
      return CardValue.EIGHT;
    } else if (number === 9) {
      return CardValue.NINE;
    }
  } else if (type === "DRAW_TWO") {
    return CardValue.DRAW_TWO;
  } else if (type === "CHOOSE") {
    return CardValue.CHOOSE_COLOR;
  } else if (type === "CHOOSE_DRAW") {
    return CardValue.CHOOSE_COLOR_4;
  } else if (type === "SKIP") {
    return CardValue.SKIP;
  } else if (type === "SWITCH") {
    return CardValue.SWITCH;
  }

  console.log(`Type. ${type}   number: ${number} convertion to card failed`);
  throw Error();
}

export type CardValue_ = (typeof CardValue)[keyof typeof CardValue];

interface Props {
  value: CardValue_;
  color: CardColor;
  className?: string;
}

export default function Card({ value, color, className }: Props): JSX.Element {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlSpace="preserve"
      width="100%"
      height="100%"
      viewBox="0 0 58 91"
      className={className + " "}
    >
      <defs>
        <path id="a" d="M269.857 404.206h38.22v42.081h-38.22z" />
        <path id="b" d="M269.857 404.206h38.22v42.081h-38.22z" />
        <path id="c" d="M269.857 404.206h38.22v42.081h-38.22z" />
      </defs>
      <g transform="translate(-117.569 -111.032)">
        <path
          fill="#3a4142"
          stroke="#eee"
          strokeWidth="2"
          d="m174.57 180.032-21.001 21h15.484a5.504 5.504 0 0 0 5.516-5.516z"
        />
        <path
          fill={color}
          stroke="#eee"
          strokeWidth="2"
          d="m174.57 165.032-36.001 36h15l21-21z"
        />
        <path
          fill="#3a4142"
          stroke="#eee"
          strokeWidth="2"
          d="m152.704 112.032-34.135 34.135v49.349a5.504 5.504 0 0 0 5.516 5.515h14.484l36-36v-47.483a5.504 5.504 0 0 0-5.516-5.516zM137.569 112.032h-13.484a5.504 5.504 0 0 0-5.516 5.516v13.484z"
        />
        <path
          fill={color}
          stroke="#eee"
          strokeWidth="2"
          d="m137.569 112.032-19 19v15l34-34z"
        />
        <text
          xmlSpace="preserve"
          fill="#eee"
          strokeWidth="7.559"
          fontFamily="Bahnschrift"
          fontSize={value.fontSize}
          fontWeight="700"
          transform="matrix(.26458 0 0 .26458 47.191 6.435)"
        >
          <tspan x={280 + value.x1Offset} y={430 + value.y1Offset}>
            {value.text}
          </tspan>
        </text>
        <text
          xmlSpace="preserve"
          fill="#eee"
          strokeWidth="7.559"
          fontFamily="Bahnschrift"
          fontSize={value.fontSize}
          fontWeight="700"
          transform="matrix(.26458 0 0 .26458 92.39 82.63)"
        >
          <tspan x={280 + value.x2Offset} y={430 + value.y2Offset}>
            {value.text}
          </tspan>
        </text>
        <ellipse
          cx="209.255"
          cy="49.64"
          fill="#eee"
          stroke="#eee"
          strokeWidth="2.014"
          rx="14.419"
          ry="29.162"
          transform="matrix(.83529 .54981 -.57222 .8201 0 0)"
        />
        <text
          xmlSpace="preserve"
          fill="#3a4142"
          strokeWidth="7.559"
          fontFamily="Bahnschrift"
          fontSize={value.fontSize}
          fontWeight="700"
          transform="matrix(.9191 0 0 .9191 -119.728 -233.293)"
        >
          <tspan x={280 + value.x3Offset} y={430 + value.y3Offset}>
            {value.text}
          </tspan>
        </text>
      </g>
    </svg>
  );
}
