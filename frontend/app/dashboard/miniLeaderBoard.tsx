"use client";

import MiniLeaderBoardContent from "./miniLeaderBoardContent";
import { leaderBoardEntry } from "../types/leaderBoardEntry";
import React, { useState } from "react";

interface Props {
  heading: string;
}

// TODO: Remove this as this is just mocked data to test the frontend
const exampleEntries: leaderBoardEntry[] = [
  { place: 1, score: 90, botName: "Lewisbot", userName: "User1" },
  { place: 2, score: 85, botName: "Goropogo", userName: "User2" },
  { place: 3, score: 80, botName: "Greenpop", userName: "User3" },
  { place: 4, score: 75, botName: "ThisIsAnAwesome name", userName: "User4" },
  { place: 5, score: 70, botName: "Bot5", userName: "User5" },
];

const exampleEntries1: leaderBoardEntry[] = [
  { place: 1, score: 95, botName: "BotA", userName: "UserA" },
  { place: 2, score: 88, botName: "BotB", userName: "UserB" },
  { place: 3, score: 82, botName: "BotC", userName: "UserC" },
  { place: 4, score: 76, botName: "BotD", userName: "UserD" },
  { place: 5, score: 70, botName: "BotE", userName: "UserE" },
];

// Example Entry List 2
const exampleEntries2: leaderBoardEntry[] = [
  { place: 1, score: 89, botName: "BotX", userName: "UserX" },
  { place: 2, score: 83, botName: "BotY", userName: "UserY" },
  { place: 3, score: 77, botName: "BotZ", userName: "UserZ" },
  { place: 4, score: 71, botName: "BotW", userName: "UserW" },
  { place: 5, score: 65, botName: "BotV", userName: "UserV" },
];

export default function MiniLeaderBoard({ heading }: Props): JSX.Element {
  const [selectedTab, setSelectedTab] = useState("tabs-global");

  return (
    <div className="bg-secondary rounded-lg">
      <ul
        className="mb-5 flex list-none flex-row flex-wrap border-b-0 pl-0"
        role="tablist"
        data-te-nav-ref
      >
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary rounded-tl-lg ${
            selectedTab === "tabs-global"
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-global"
            aria-controls="tabs-global"
            aria-selected={selectedTab === "tabs-global"}
            onClick={() => setSelectedTab("tabs-global")}
          >
            Global
          </a>
        </li>
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary ${
            selectedTab === "tabs-continent"
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-continent"
            aria-controls="tabs-continent"
            aria-selected={selectedTab === "tabs-continent"}
            onClick={() => setSelectedTab("tabs-continent")}
          >
            Continent
          </a>
        </li>
        <li
          role="presentation"
          className={`flex-auto text-center bg-primary rounded-tr-lg ${
            selectedTab === "tabs-country"
              ? "text-white border-b-2 border-white"
              : "bg-primary"
          }`}
        >
          <a
            className="my-2 block px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight"
            data-te-toggle="pill"
            data-te-target="#tabs-country"
            aria-controls="tabs-country"
            aria-selected={selectedTab === "tabs-country"}
            onClick={() => setSelectedTab("tabs-country")}
          >
            Country
          </a>
        </li>
      </ul>

      <div className="mb-6">
        <div
          style={{ display: selectedTab === "tabs-global" ? "block" : "none" }}
          id="tabs-global"
          role="tabpanel"
          aria-labelledby="tabs-global"
          data-te-tab-active
        >
          <MiniLeaderBoardContent title={heading} entries={exampleEntries} />
        </div>
        <div
          style={{
            display: selectedTab === "tabs-continent" ? "block" : "none",
          }}
          id="tabs-continent"
          role="tabpanel"
          aria-labelledby="tabs-continent"
        >
          <MiniLeaderBoardContent title={heading} entries={exampleEntries1} />
        </div>
        <div
          style={{
            display: selectedTab === "tabs-country" ? "block" : "none",
          }}
          id="tabs-country"
          role="tabpanel"
          aria-labelledby="tabs-country"
        >
          <MiniLeaderBoardContent title={heading} entries={exampleEntries2} />
        </div>
      </div>
    </div>
  );
}
