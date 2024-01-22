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
  deleteBot as _deleteBot,
  rankBot as _rankBot,
} from "@/app/services/BotService";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { UnAuthorizedError } from "@/app/exceptions/UnAuthorizedError";
import { NotFoundError } from "@/app/exceptions/NotFoundError";

const CODE_CHARACTER_LIMIT = 32000;

function saveNewBot(
  name: string,
  code: string,
  setId: (id: number) => void,
  router: any
): void {
  createBot(name, code)
    .then((id) => {
      setId(id);
      router.replace("/bot/editor/" + String(id));
    })
    .catch(() => {});
}

function saveBot(id: number, code: string): void {
  _saveBot(id, code)
    .then(() => {})
    .catch(() => {});
}

function rankBot(id: number): void {
  _rankBot(id)
    .then(() => {
      toast.success("This bot has been queued for ranking");
    })
    .catch(() => {
      toast.error("Bot could not be queued for ranking");
    });
}

function deleteBot(id: number, router: any): void {
  _deleteBot(id)
    .then(() => {
      router.replace("/bot");
    })
    .catch(() => {});
}

interface Props {
  id?: number | null;
}
export default function BotEditor({ id = null }: Props): JSX.Element {
  const [_id, setId] = useState<number | null>(id);
  const [name, setName] = useState("");
  const [code, setCode] = useState<string | undefined>(undefined);
  const [saved, setSaved] = useState(true);
  const [ranked, setRanked] = useState(false);
  const router = useRouter();

  const setupBotCodeTemplate = async (): Promise<string> => {
    try {
      const response = await fetch("../codeTemplate.txt");
      if (!response.ok) {
        console.error(response);
        throw Error();
      }
      return await response.text();
    } catch (error) {
      toast.error("Failed to load new code template");
      return "";
    }
  };

  useEffect(() => {
    if (_id == null) {
      // fetch new name

      getNewBotName()
        .then((n) => {
          setName(n);
          setupBotCodeTemplate()
            .then((codeTemplate: string) => {
              setCode(codeTemplate);
            })
            .catch(() => {
              toast.error("Error loading code template");
            });
        })
        .catch(() => {});
    } else {
      getBot(_id)
        .then((b: Bot) => {
          setName(b.name);
          setCode(b.currentCode);
        })
        .catch((ex) => {
          if (ex instanceof UnAuthorizedError) {
            router.replace("/unauthorized");
          }
          if (ex instanceof NotFoundError) {
            router.replace("/bot");
            toast.error("The bot you tried to see does not exist!", {
              toastId: 2432502340,
            });
          }
        });
    }
  }, [_id]);

  useEffect(() => {
    const handleBeforeUnload = (event: BeforeUnloadEvent): void => {
      if (!saved) {
        event.preventDefault();
        event.returnValue = "";
      }
    };
    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [saved]);

  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title={name} subTitle="Bot-Name" />
      <div className="h-full w-11/12">
        <EditorButtons
          save={() => {
            if (code !== undefined && code?.length >= CODE_CHARACTER_LIMIT) {
              toast.error(
                `This bot exceeds the character limit of ${CODE_CHARACTER_LIMIT}`
              );
              return;
            }
            setSaved(true);
            if (_id === null) {
              saveNewBot(name, code ?? "", setId, router);
            } else {
              saveBot(_id, code ?? "");
            }
          }}
          rank={() => {
            if (_id === null || !saved) {
              toast.error("Bot needs to be saved before it can be ranked");
              return;
            }
            if (code === undefined) {
              toast.error("No code provided");
              return;
            }
            if (ranked) {
              toast.error("This bot has already been queued for ranking");
              return;
            }
            if (code?.length >= CODE_CHARACTER_LIMIT) {
              toast.error(
                `This bot exceeds the character limit of ${CODE_CHARACTER_LIMIT}`
              );
              return;
            }
            rankBot(_id);
            setRanked(true);
          }}
          _delete={() => {
            if (_id !== null) {
              deleteBot(_id, router);
            } else {
              toast.success("Bot deleted");
              router.replace("/bot");
            }
          }}
        />
        <CodeEditor
          code={code ?? null}
          onChange={(c) => {
            if (c !== code) {
              setSaved(false);
              setRanked(false);
            }
            setCode(c ?? "");
          }}
        />
      </div>
      <LoggingElement />
    </div>
  );
}
