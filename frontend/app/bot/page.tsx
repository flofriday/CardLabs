"use client";

import { useEffect, useState } from "react";
import BotCard from "./botCard";
import { Bot, getAllBots } from "../services/BotService";
import Pagination from "../components/Pagination";
import Link from "next/link";

export default function BotOverview(): JSX.Element {
  const [page, setPage] = useState(0);
  const [numPages, setNumPages] = useState(0);
  const [bots, setBots] = useState<Bot[]>([]);

  const fetch = (): void => {
    getAllBots(page, 6)
      .then((fpage) => {
        setBots(fpage.content);
        setNumPages(fpage.totalPages);
        if (page >= fpage.totalPages) {
          setPage(fpage.totalPages - 1);
        }
      })
      .catch(() => {});
  };
  useEffect(() => {
    getAllBots(page, 6)
      .then((fpage) => {
        setBots(fpage.content);
        setNumPages(fpage.totalPages);
        if (page >= fpage.totalPages) {
          setPage(fpage.totalPages - 1);
        }
      })
      .catch(() => {});
  }, [page]);

  return (
    <>
      <h1
        id="leftHeading"
        className="absolute text-5xl md:text-7xl mt-6 md:mt-12 ml-10 md:ml-16 font-medium tracking-wider inline-block w-fit"
      >
        My Bots
      </h1>
      {bots.length > 0 ? (
        <div className=" w-full flex items-center justify-center flex-col mt-28">
          <Link
            href="/bot/editor"
            className="btn bg-primary p-2 font-bold rounded-lg shadow-md text-3xl hover:bg-primary_highlight ml-4 flex items-center"
            id="button_create_new_bot_default"
          >
            Add new bot
          </Link>
          <div className="mt-4 w-9/12 grid grid-cols-1 lg:grid-cols-2 2xl:grid-cols-3 gap-8">
            {bots.map((b, idx) => {
              return <BotCard key={idx} bot={b} refetch={fetch} />;
            })}
          </div>
          <div className="flex items-center mb-4">
            <Pagination
              onPageChange={(p) => {
                setPage(p);
              }}
              totalNumberOfPages={numPages}
              initalPage={page}
            />
          </div>
        </div>
      ) : (
        <div className="flex items-center justify-center h-screen">
          <div className="flex flex-col items-center">
            <h2 className="text-8xl font-extrabold text-center mb-4">
              No bots here yet :)
            </h2>
            <Link
              href="/bot/editor"
              className="btn bg-primary p-4 font-bold rounded-lg shadow-md text-4xl  hover:bg-primary_highlight"
              id="button_create_new_bot"
            >
              Add a bot now
            </Link>
          </div>
        </div>
      )}
    </>
  );
}
