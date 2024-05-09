import styles from './MenuList.module.css';
import { MenuListProps } from './MenuList.props';
import ProductCard from '../../../components/ProductCard/ProductCard';

export function MenuList({ products }: MenuListProps) {
	return <div className={styles.wrapper}>{products.map(p => (
		<ProductCard
			key={p.id}
			id={p.id}
			name={p.name}
			description={p.description}
			rating={p.rating}
			price={p.price}
			image={p.image}
		/>
	))}
	</div>;
}