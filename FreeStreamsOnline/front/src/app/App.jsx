import { HashRouter, Route, Routes } from "react-router-dom";
import { MainLayout } from "./layout/MainLayout";
import { AccountPage } from "./pages/AccountPage";
import { CategoryPage } from "./pages/CategoryPage";
import { FormPage } from "./pages/FormPage";
import { MainPage } from "./pages/MainPage";
import { SavedStreamsPage } from "./pages/SavedStreamsPage";
import { SubscriptionsPage } from "./pages/SubscriptionsPage";

import { NotFoundPage } from "./pages/NotFoundPage";

export const App = () => {
    return (
        <HashRouter>
            <Routes>
                <Route element={<MainLayout />}>
                    <Route index element={<MainPage />} />
                    <Route path="/form" element={<FormPage />} />
                    <Route path="/form/:id" element={<FormPage />} /> 
                    <Route path="/category" element={<CategoryPage />} />
                    <Route path="/account" element={<AccountPage />} />
                    <Route path="/subscriptions" element={<SubscriptionsPage />} />
                    <Route path="/savedStreams" element={<SavedStreamsPage />} />
                    <Route path="*" element={<NotFoundPage />} />
                </Route>
            </Routes>
        </HashRouter>
    );
};
