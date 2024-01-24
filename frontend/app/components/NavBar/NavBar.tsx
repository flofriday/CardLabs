"use client";
/* eslint-disable @next/next/no-async-client-component */

import React, { useEffect } from "react";
import Link from "next/link";
import SignInUp from "./SignInUp";
import Profile from "./Profile";
import { isAuthenticated } from "@/app/services/AuthenticationService";
import { looseUnsavedChanges } from "@/app/services/NavigationService";
import { useSaveCodeStore } from "@/app/state/savedCodeStore";
import { authenticationStore } from "@/app/state/authenticationStore";
import { toast } from "react-toastify";

export default function NavBar(): JSX.Element {
  const authenticated: boolean = authenticationStore(
    (state: any) => state.authenticated
  );
  const setAuthenticated = authenticationStore(
    (state: any) => state.setAuthenticated
  );
  const codeSaved: boolean = useSaveCodeStore((state: any) => state.codeSaved);
  const setCodeSaved = useSaveCodeStore((state: any) => state.setCodeSaved);

  useEffect(() => {
    setAuthenticated(false);
  }, []);

  useEffect(() => {
    isAuthenticated()
      .then((auth) => {
        setAuthenticated(auth);
      })
      .catch(() => {
        toast.error("Error loading authentication status");
      });
  }, [authenticated]);

  return (
    <div className="w-full h-14 bg-primary flow-root fixed z-30">
      <div className="h-full flex items-center space-x-6 w-fit float-left">
        {authenticated ? (
          <Link
            id="home_authenticated"
            href="/dashboard"
            className="mx-3 tracking-[.3em] font-bold text-[1.6em]"
            onClick={(e) => {
              looseUnsavedChanges(e, codeSaved, setCodeSaved);
            }}
          >
            Card Labs
          </Link>
        ) : (
          <Link
            id="home_unauthenticated"
            href="/"
            className="mx-3 tracking-[.3em] font-bold text-[1.6em]"
            onClick={(e) => {
              looseUnsavedChanges(e, codeSaved, setCodeSaved);
            }}
          >
            Card Labs
          </Link>
        )}
        {authenticated && (
          <Link
            href="/bot"
            id="mybots_link"
            className="text-base  hover:text-accent max-sm:hidden"
            onClick={(e) => {
              looseUnsavedChanges(e, codeSaved, setCodeSaved);
            }}
          >
            My Bots
          </Link>
        )}
        <Link
          href="/leaderboard"
          id="leaderboard_link"
          className="text-base  hover:text-accent max-sm:hidden"
          onClick={(e) => {
            looseUnsavedChanges(e, codeSaved, setCodeSaved);
          }}
        >
          Leaderboard
        </Link>

        {authenticated && (
          <Link
            href="/myLeaderboard"
            id="myleaderboard_link"
            className="text-base  hover:text-accent max-sm:hidden"
            onClick={(e) => {
              looseUnsavedChanges(e, codeSaved, setCodeSaved);
            }}
          >
            My Leaderboard
          </Link>
        )}
        <Link
          href="/help"
          className="text-base  hover:text-accent max-sm:hidden"
          onClick={(e) => {
            looseUnsavedChanges(e, codeSaved, setCodeSaved);
          }}
        >
          Help
        </Link>
        <Link
          href="/rules"
          className="text-base  hover:text-accent max-sm:hidden"
          onClick={(e) => {
            looseUnsavedChanges(e, codeSaved, setCodeSaved);
          }}
        >
          Rules
        </Link>
      </div>
      <div className="w-fit flex h-full float-right items-center mr-5">
        {authenticated ? <Profile /> : <SignInUp />}
      </div>
    </div>
  );
}
