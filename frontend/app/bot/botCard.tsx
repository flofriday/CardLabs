"use client";

import { useEffect, useState } from "react";
import IconButton, { ButtonIcon } from "../components/iconButton";
import { IoClose } from "react-icons/io5";
import Modal from "../components/modal";
import { RegionType } from "../types/RegionType";
import Link from "next/link";
import {
  deleteBot as _deleteBot,
  Bot,
  getBotRank,
} from "@/app/services/BotService";

interface Props {
  bot: Bot;
  refetch: () => void;
}

function deleteBot(id: number, refetch: () => void): void {
  _deleteBot(id)
    .then(() => {
      refetch();
    })
    .catch(() => {});
}

export default function BotCard({ bot, refetch }: Props): JSX.Element {
  const [name, setName] = useState(bot.name);
  const [score, setScore] = useState(bot.eloScore);
  const [rankGlobal, setRankGlobal] = useState(-1);
  const [rankContinent, setRankContinent] = useState(-1);
  const [rankCountry, setRankCountry] = useState(-1);
  const [createdDate, setCreatedDate] = useState(bot.created.toLocaleString());
  const [updatedDate, setUpdatedDate] = useState(bot.updated.toLocaleString());
  const [deleteModalVisibility, setDeleteAccountModalVisibility] =
    useState(false);

  useEffect(() => {
    getBotRank(bot.id, RegionType.GLOBAL)
      .then((r) => {
        setRankGlobal(r);
      })
      .catch(() => {});
    getBotRank(bot.id, RegionType.CONTINENT)
      .then((r) => {
        setRankContinent(r);
      })
      .catch(() => {});

    getBotRank(bot.id, RegionType.COUNTRY)
      .then((r) => {
        setRankCountry(r);
      })
      .catch(() => {});
    setName(bot.name);
    setScore(bot.eloScore);
    setCreatedDate(bot.created.toLocaleString());
    setUpdatedDate(bot.updated.toLocaleString());
  }, [bot.id, bot.name, bot.eloScore, bot.created, bot.updated]);
  return (
    <>
      {deleteModalVisibility && (
        <div className="absolute top-0 left-0">
          <Modal
            title="Delete Bot"
            modalClassName="w-3/12"
            onClose={() => {
              setDeleteAccountModalVisibility(false);
            }}
          >
            <div>
              Do your really want to delete your bot {name}?
              <div className="flex justify-around pt-4">
                <button
                  className="btn bg-accent text-text py-2 w-48 rounded-lg shadow-md text-lg"
                  id="delete_modal_delete_button"
                  onClick={() => {
                    deleteBot(bot.id, refetch);
                    setDeleteAccountModalVisibility(false);
                  }}
                >
                  Delete Bot
                </button>
                <button
                  className="btn bg-secondary text-text py-2 w-48 rounded-lg shadow-md text-lg"
                  id="delete_modal_back_button"
                  onClick={() => {
                    setDeleteAccountModalVisibility(false);
                  }}
                >
                  Go back
                </button>
              </div>
            </div>
          </Modal>
        </div>
      )}
      <div className="relative bg-primary rounded-xl w-full h-full p-4 flex flex-col justify-between">
        <button
          className="absolute right-3 top-2"
          onClick={() => {
            setDeleteAccountModalVisibility(true);
          }}
        >
          <IoClose size={40} style={{ fill: "#87153A" }} />
        </button>
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
          <Link href={"/"}>
            <IconButton
              text="Info"
              type={ButtonIcon.INFO}
              width={"w-24"}
              hover="hover:bg-primary_highlight"
            />
          </Link>
          <Link href={"/"}>
            <IconButton
              text="Rank Bot"
              type={ButtonIcon.RANK}
              hover="hover:bg-primary_highlight"
            />
          </Link>
          <Link href={"/bot/editor/" + bot.id}>
            <IconButton
              text="Edit"
              type={ButtonIcon.EDIT}
              width={"w-24"}
              hover="hover:bg-primary_highlight"
            />
          </Link>
        </div>
      </div>
    </>
  );
}
