package br.com.zup.bootcamp.ecommerce.product;

import java.net.URL;
import java.util.Collection;

interface ImageStorage {

    Collection<URL> upload(Collection<ImageFile> files);
}
