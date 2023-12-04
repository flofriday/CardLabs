"use client";

import LeftPageHeader from "@/app/components/leftPageHeader";
import EditorButtons from "./editorButtons";
import LoggingElement from "./loggingElement";
import CodeEditor from "./codeEditor";
import { useEffect, useState } from "react";
import {
  getNewBotName,
  createBot,
  getBot,
  Bot,
  saveBot as _saveBot,
} from "@/app/services/BotService";
import { useRouter } from "next/navigation";

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

function saveBot(id: number, code: string | null): void {
  _saveBot(id, code)
    .then((r) => {})
    .catch(() => {});
}

export default function BotEditor({ id = undefined }: Props): JSX.Element {
  const [_id, setId] = useState(id);
  const [name, setName] = useState("");
  const [code, setCode] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    if (_id == null) {
      // fetch new name
      getNewBotName()
        .then((n) => {
          setName(n);
        })
        .catch(() => {});
    } else {
      getBot(_id)
        .then((b: Bot) => {
          setName(b.name);
          setCode(b.currentCode);
        })
        .catch(() => {});
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
              if (_id !== undefined) {
                router.replace("/bot/editor/" + String(_id));
              }
            } else {
              saveBot(_id, code);
            }
          }}
        />
        <CodeEditor code={code} onChange={setCode} />
      </div>
      <LoggingElement />
    </div>
  );
}
