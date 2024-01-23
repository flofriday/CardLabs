"use client";

import { useEffect, useState } from "react";
import LeftPageHeader from "../components/leftPageHeader";
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
      <LeftPageHeader title="My Bots" />
      {bots.length > 0 ? (
        <div className=" w-full flex items-center justify-center flex-col mt-28">
          <div className="mt-14 w-9/12 grid grid-cols-1 lg:grid-cols-2 2xl:grid-cols-3 gap-8">
            {bots.map((b, idx) => {
              return <BotCard key={idx} bot={b} refetch={fetch} />;
            })}
          </div>
          <div className="flex items-center">
            <Pagination
              onPageChange={(p) => {
                setPage(p);
              }}
              totalNumberOfPages={numPages}
              initalPage={page}
            />
            <Link
              href="/bot/editor"
              className="btn bg-primary_highlight p-2 font-bold rounded-lg shadow-md text-4xl hover:bg-primary ml-4 mt-4 flex items-center"
              id="button_create_new_bot_default"
            >
              New bot
            </Link>
          </div>
        </div>
      ) : (
        <div className="flex items-center justify-center h-screen">
          <div className="flex flex-col items-center">
            <h2 className="text-8xl font-extrabold text-center mb-4">
              No scored bots here yet :)
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
