"use client";

import BotEditor from "../page";
import { notFound } from "next/navigation";
export default function BotWithIdEditor({
  params,
}: {
  params: { slug: string };
}): JSX.Element {
  if (isNaN(Number(params.slug))) {
    return notFound();
  }

  return <BotEditor id={Number(params.slug)} />;
}
