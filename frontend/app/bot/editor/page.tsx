"use client";

import LeftPageHeader from "@/app/components/leftPageHeader";
import EditorButtons from "./editorButtons";
import LoggingElement from "./loggingElement";
import CodeEditor from "./codeEditor";
import { useEffect, useState } from "react";
import { getNewBotName, createBot } from "@/app/services/BotService";

interface Props {
  id?: number;
}

function saveNewBot(
  name: string,
  code: string | null,
  setId: (id: number) => void
): void {
  createBot(name, code)
    .then((id) => {
      setId(id);
    })
    .catch(() => {});
}

export default function BotEditor({ id }: Props): JSX.Element {
  const [_id, setId] = useState(id);
  const [name, setName] = useState("");
  const [code, setCode] = useState<string | null>(null);

  useEffect(() => {
    if (_id == null) {
      // fetch new name
      getNewBotName()
        .then((n) => {
          setName(n);
        })
        .catch(() => {});
    } else {
      // fetch bot
      setName("Name 2");
    }
  }, [_id]);

  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title={name} subTitle="Bot-Name" />
      <div className="h-full w-8/12">
        <EditorButtons
          save={() => {
            if (_id == null) {
              saveNewBot(name, code, setId);
            }
          }}
        />
        <CodeEditor onChange={setCode} />
      </div>
      <LoggingElement />
    </div>
  );
}
