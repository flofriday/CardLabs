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
  rankBot,
  createTestMatch,
  CodeHistory,
} from "@/app/services/BotService";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";
import { UnAuthorizedError } from "@/app/exceptions/UnAuthorizedError";
import { NotFoundError } from "@/app/exceptions/NotFoundError";
import { useSaveCodeStore } from "@/app/state/savedCodeStore";

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

function botRankWrapper(id: number): void {
  rankBot(id)
    .then(() => {
      toast.success("Successfully queued the bot for ranking");
    })
    .catch(() => {
      toast.error("An error occurred Please try again later");
    });
}

function testMatchWrapper(botId: number): void {
  createTestMatch(botId)
    .then(() => {
      toast.success(
        "Successfully queued a test match. You can view the match results in the match history."
      );
    })
    .catch(() => {});
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
  const [codeHistory, setCodeHistory] = useState<CodeHistory[]>([]);
  const [isHistoryMode, setHistoryMode] = useState(false);
  const router = useRouter();
  const codeSaved: boolean = useSaveCodeStore((state: any) => state.codeSaved);
  const setCodeSaved = useSaveCodeStore((state: any) => state.setCodeSaved);

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
          const history = b.codeHistory;
          history.unshift({ botId: b.id, code: b.currentCode, id: -1 });
          setCodeHistory(history);
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
  }, [_id, router]);

  useEffect(() => {
    const handleBeforeUnload = (event: BeforeUnloadEvent): void => {
      if (!codeSaved) {
        event.preventDefault();
        event.returnValue = "";
      }
    };
    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [codeSaved]);

  return (
    <div className="w-full h-full flex">
      <LeftPageHeader title={name} subTitle="Bot-Name" />
      <div className="h-full w-11/12">
        <EditorButtons
          codeHistory={codeHistory}
          onCodeChange={(c, histMode) => {
            setHistoryMode(histMode);
            setCode(c);
            if (!histMode) {
              codeHistory[0].code = c;
              setCodeHistory(codeHistory);
            }
          }}
          save={() => {
            if (code !== undefined && code?.length >= CODE_CHARACTER_LIMIT) {
              toast.error(
                `This bot exceeds the character limit of ${CODE_CHARACTER_LIMIT}`
              );
              return;
            }
            setCodeSaved(true);
            if (_id === null) {
              saveNewBot(name, code ?? "", setId, router);
            } else {
              saveBot(_id, code ?? "");
              codeHistory[0].code = code + "";
              setCodeHistory(codeHistory);
            }
          }}
          rank={() => {
            if (code === undefined) {
              toast.error("No code provided");
              return;
            }
            if (code?.length >= CODE_CHARACTER_LIMIT) {
              toast.error(
                `This bot exceeds the character limit of ${CODE_CHARACTER_LIMIT}`
              );
              return;
            }
            if (_id !== null) {
              _saveBot(_id, code)
                .then(() => {
                  botRankWrapper(_id);
                })
                .catch(() => {});
            } else {
              createBot(name, code)
                .then(() => {
                  if (_id !== null) {
                    botRankWrapper(_id);
                  } else {
                    toast.error("The bot needs to be saved before ranking");
                  }
                })
                .catch(() => {});
            }
          }}
          test={() => {
            if (code === undefined) {
              toast.error("No code provided");
              return;
            }
            if (code?.length >= CODE_CHARACTER_LIMIT) {
              toast.error(
                `This bot exceeds the character limit of ${CODE_CHARACTER_LIMIT}`
              );
              return;
            }

            if (_id !== null) {
              _saveBot(_id, code)
                .then(() => {
                  testMatchWrapper(_id);
                })
                .catch(() => {});
            } else {
              createBot(name, code)
                .then(() => {
                  if (_id !== null) {
                    testMatchWrapper(_id);
                  } else {
                    toast.error("The bot needs to be saved before testing");
                  }
                })
                .catch(() => {});
            }
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
          readOnly={isHistoryMode}
          onChange={(c) => {
            if (c !== code) {
              setCodeSaved(false);
            }
            setCode(c ?? "");
            if (!isHistoryMode && codeHistory[0] !== undefined) {
              codeHistory[0].code = c ?? "";
              setCodeHistory(codeHistory);
            }
          }}
        />
      </div>
      <LoggingElement />
    </div>
  );
}
