import {ChangeEvent, useEffect, useState} from 'react';
import axios, { AxiosError } from 'axios';
import styles from './Menu.module.css';
import { SERVER } from '../../helper/variables';
import { IProduct } from '../../interfaces/product.interface';
import Headling from '../../components/Headling/Headling';
import Search from '../../components/Search/Search';
import { MenuList } from './MenuList/MenuList';
import {store} from "../../store/store.ts";

export function Menu() {
	const [products, setProducts] = useState<IProduct[]>([]);
	const [isLoading, setIsLoading] = useState<boolean>(false);
	const [error, setError] = useState<string | undefined>();
	const [filter, setFilter] = useState<string>();

	useEffect(() => {
		getMenu(filter);
	}, [filter]);

	const getMenu = async (name?: string) => {
		try {
			setIsLoading(true);
			const jwt = store.getState().user.jwt
			const { data } = await axios.get<IProduct[]>(`${SERVER}/product`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				},
				params: {
					name
				}
			});
			setProducts(data);
			setIsLoading(false);
		} catch (e) {
			console.error(e);
			if (e instanceof AxiosError) {
				setError(e.message);
			}
			setIsLoading(false);
			return;
		}
	};

	const updateFilter = (e: ChangeEvent<HTMLInputElement>) => {
		setFilter(e.target.value);
	};


	return <>
		<div className={styles['head']}>
			<Headling>Menu</Headling>
			<Search placeholder='Search' onChange={updateFilter} />
		</div>
		<div>
			{error && <>{error}</>}
			{!isLoading && products.length > 0 && <MenuList products={products} />}
			{isLoading && <>Loading products...</>}
			{!isLoading && products.length === 0 && <>No products found for your request</>}
		</div>
	</>;
}

export default Menu;