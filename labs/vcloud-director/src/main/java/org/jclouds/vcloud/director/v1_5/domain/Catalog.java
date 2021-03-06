/*
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.domain;

import static com.google.common.base.Preconditions.*;
import static org.jclouds.vcloud.director.v1_5.VCloudDirectorConstants.*;

import java.net.URI;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;

import com.google.common.collect.Sets;

/**
 * Container for references to VappTemplate and Media objects.
 *
 * <pre>
 * &lt;complexType name="CatalogType" /&gt;
 * </pre>
 *
 * @author grkvlt@apache.org
 */
@XmlRootElement(namespace = VCLOUD_1_5_NS, name = "Catalog")
public class Catalog extends EntityType<Catalog> {

   public static final String MEDIA_TYPE = VCloudDirectorMediaType.CATALOG;

   @SuppressWarnings("unchecked")
   public static Builder builder() {
      return new Builder();
   }

   @Override
   public Builder toBuilder() {
      return new Builder().fromCatalog(this);
   }

   public static class Builder extends EntityType.Builder<Catalog> {

      private Entity owner;
      private CatalogItems catalogItems;
      private Boolean isPublished;

      /**
       * @see Catalog#getOwner()
       */
      public Builder owner(Entity owner) {
         this.owner = owner;
         return this;
      }

      /**
       * @see Catalog#getCatalogItems()
       */
      public Builder catalogItems(CatalogItems catalogItems) {
         this.catalogItems = catalogItems;
         return this;
      }

      /**
       * @see Catalog#isPublished()
       */
      public Builder isPublished(Boolean isPublished) {
         this.isPublished = isPublished;
         return this;
      }

      /**
       * @see Catalog#isPublished()
       */
      public Builder published() {
         this.isPublished = Boolean.TRUE;
         return this;
      }

      @Override
      public Catalog build() {
         Catalog catalog = new Catalog(href, name);
         catalog.setOwner(owner);
         catalog.setCatalogItems(catalogItems);
         catalog.setIsPublished(isPublished);
         catalog.setDescription(description);
         catalog.setId(id);
         catalog.setType(type);
         catalog.setLinks(links);
         catalog.setTasksInProgress(tasksInProgress);
         return catalog;
      }

      /**
       * @see EntityType#getName()
       */
      @Override
      public Builder name(String name) {
         this.name = name;
         return this;
      }

      /**
       * @see EntityType#getDescription()
       */
      @Override
      public Builder description(String description) {
         this.description = description;
         return this;
      }

      /**
       * @see EntityType#getId()
       */
      @Override
      public Builder id(String id) {
         this.id = id;
         return this;
      }

      /**
       * @see EntityType#getTasksInProgress()
       */
      @Override
      public Builder tasksInProgress(TasksInProgress tasksInProgress) {
         this.tasksInProgress = tasksInProgress;
         return this;
      }

      /**
       * @see ReferenceType#getHref()
       */
      @Override
      public Builder href(URI href) {
         this.href = href;
         return this;
      }

      /**
       * @see ReferenceType#getType()
       */
      @Override
      public Builder type(String type) {
         this.type = type;
         return this;
      }

      /**
       * @see ReferenceType#getLinks()
       */
      @Override
      public Builder links(Set<Link> links) {
         this.links = Sets.newLinkedHashSet(checkNotNull(links, "links"));
         return this;
      }

      /**
       * @see ReferenceType#getLinks()
       */
      @Override
      public Builder link(Link link) {
         this.links.add(checkNotNull(link, "link"));
         return this;
      }

      @Override
      public Builder fromEntityType(EntityType<Catalog> in) {
         return Builder.class.cast(super.fromEntityType(in));
      }

      public Builder fromCatalog(Catalog in) {
         return fromEntityType(in).owner(in.getOwner()).catalogItems(in.getCatalogItems()).isPublished(in.isPublished());
      }
   }

   private Catalog() {
      // For JAXB and builder use
   }

   private Catalog(URI href, String name) {
      super(href, name);
   }

   @XmlElement(name = "Owner")
   private Entity owner;
   @XmlElement(name = "CatalogItems")
   private CatalogItems catalogItems;
   @XmlElement(name = "IsPublished")
   private Boolean isPublished;

   /**
    * Gets the value of the owner property.
    */
   public Entity getOwner() {
      return owner;
   }

   public void setOwner(Entity value) {
      this.owner = value;
   }

   /**
    * Gets the value of the catalogItems property.
    */
   public CatalogItems getCatalogItems() {
      return catalogItems;
   }

   public void setCatalogItems(CatalogItems value) {
      this.catalogItems = value;
   }

   /**
    * Gets the value of the isPublished property.
    */
   public Boolean isPublished() {
      return isPublished;
   }

   public void setIsPublished(Boolean value) {
      this.isPublished = value;
   }
}
