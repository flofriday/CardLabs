"use client";

import { useState } from "react";
import IconButton, { ButtonIcon } from "../components/iconButton";

export default function BotCard(): JSX.Element {
  const [name, setName] = useState("Example Name");
  const [score, setScore] = useState(1804);
  const [rankGlobal, setRankGlobal] = useState(541);
  const [rankContinent, setRankContinent] = useState(210);
  const [rankCountry, setRankCountry] = useState(30);
  const [createdDate, setCreatedDate] = useState("01.02.2024");
  const [updatedDate, setUpdatedDate] = useState("03.02.2024");

  return (
    <div className="bg-primary rounded-xl w-full h-full p-4 flex flex-col justify-between">
      <h1 className="text-text text-3xl mb-3 font-bold">{name}</h1>
      <div className="grid grid-cols-2 ml-6">
        <h3>Score:</h3>
        <h3>{score}</h3>
        <h3>Rank Global:</h3>
        <h3>{rankGlobal}</h3>
        <h3>Rank Continent:</h3>
        <h3>{rankContinent}</h3>
        <h3>Rank Country:</h3>
        <h3>{rankCountry}</h3>
        <h3>Created:</h3>
        <h3>{createdDate}</h3>
        <h3>Updated:</h3>
        <h3>{updatedDate}</h3>
      </div>
      <div className="flex justify-center space-x-4 mt-4">
        <IconButton
          text="Info"
          type={ButtonIcon.INFO}
          width={"w-24"}
          hover="hover:bg-primary_highlight"
        />
        <IconButton
          text="Rank Bot"
          type={ButtonIcon.RANK}
          hover="hover:bg-primary_highlight"
        />
        <IconButton
          text="Edit"
          type={ButtonIcon.EDIT}
          width={"w-24"}
          hover="hover:bg-primary_highlight"
        />
      </div>
    </div>
  );
}
